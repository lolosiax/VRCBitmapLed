package top.lolosia.installer;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.*;
import java.util.*;

class WinResourceClassLoader extends ClassLoader {
    public WinResourceClassLoader() {
        this(null);
    }

    // 必须传入父加载器保证双亲委派机制
    public WinResourceClassLoader(ClassLoader parent) {
        super(parent);
        synchronized (WinResourceClassLoader.class) {
            if (resources == null) init();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = loadFromResource(name.replace('.', '/') + ".class");
        if (classBytes == null) throw new ClassNotFoundException(name);
        return defineClass(name, classBytes, 0, classBytes.length);
    }

    // 可选：资源加载支持
    @Override
    public InputStream getResourceAsStream(String name) {
        byte[] data = loadFromResource(name);
        return data != null ? new ByteArrayInputStream(data) : super.getResourceAsStream(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) {
        return Collections.enumeration(resources.values());
    }

    @Override
    protected URL findResource(String name) {
        return resources.get(name);
    }

    // Native 方法声明
    private static native byte[] loadFromResource(String resourcePath);

    private static native String getResourcesName(int index);

    private static native int getResourcesCount();

    private static Map<String, URL> resources;

    private static void init() {
        URL.setURLStreamHandlerFactory(protocol -> {
            if (!"winres".equals(protocol)) return null;
            return new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) {
                    return new URLConnection(u) {
                        byte[] data;

                        @Override
                        public void connect() throws FileNotFoundException {
                            var url = getURL();
                            var path = url.getPath();
                            var data = loadFromResource(path);
                            if (data == null) {
                                throw new FileNotFoundException("\"" + path + "\" can not found in windows rc resource!");
                            }
                            this.data = data;
                        }

                        @Override
                        public int getContentLength() {
                            return data.length;
                        }

                        @Override
                        public long getContentLengthLong() {
                            return data.length;
                        }

                        @Override
                        public InputStream getInputStream() {
                            return new ByteArrayInputStream(data);
                        }
                    };
                }
            };
        });

        // 初始化Jar资源列表
        var map = new LinkedHashMap<String, URL>();
        var count = getResourcesCount();
        for (int i = 0; i < count; i++) {
            var name = getResourcesName(i);
            try {
                var url = new URI("winres://" + name).toURL();
                map.put(name, url);
            } catch (URISyntaxException | MalformedURLException e) {
                System.err.println(e);
            }
        }

        resources = Collections.unmodifiableMap(map);
    }
}