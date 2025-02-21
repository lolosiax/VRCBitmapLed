package top.lolosia.installer;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;

class WinResourceClassLoader extends URLClassLoader {
    public WinResourceClassLoader() {
        this(null);
    }

    public WinResourceClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
        synchronized (WinResourceClassLoader.class) {
            if (resources == null) init();
        }
        for (URL library : libraries) addURL(library);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = loadFromResource(name.replace('.', '/') + ".class");
        if (classBytes == null) return super.findClass(name);

        return defineClass(name, classBytes, 0, classBytes.length);
    }


    @Override
    public InputStream getResourceAsStream(String name) {
        byte[] data = loadFromResource(name);
        if (data == null) return super.getResourceAsStream(name);

        return new ByteArrayInputStream(data);
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        var s = super.findResources(name);
        var l = Collections.enumeration(resources.values());
        final boolean[] isLocal = {true};
        return new Enumeration<>() {
            @Override
            public boolean hasMoreElements() {
                if (isLocal[0]) {
                    if (l.hasMoreElements()) return true;
                    isLocal[0] = false;
                }
                return s.hasMoreElements();
            }

            @Override
            public URL nextElement() {
                if (isLocal[0]) return l.nextElement();
                return s.nextElement();
            }
        };
    }

    @Override
    public URL findResource(String name) {
        if (resources.containsKey(name)) return resources.get(name);
        return super.findResource(name);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    // Native 方法声明
    private static native byte[] loadFromResource(String resourcePath);

    private static native String[] getResourcesNames();

    private static native String[] getLibraries();

    private static Map<String, URL> resources;
    private static URL[] libraries;

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
        var names = getResourcesNames();
        for (String name : names) {
            try {
                var url = new URI("winres://" + name).toURL();
                map.put(name, url);
            } catch (URISyntaxException | MalformedURLException e) {
                System.err.println(e);
            }
        }

        resources = Collections.unmodifiableMap(map);

        var libs = getLibraries();
        var urls = new URL[libs.length];
        for (int i = 0; i < libs.length; i++) {
            try {
                var lib = libs[i];
                System.out.println(lib);
                var url = new URI("file:/" + lib).toURL();
                urls[i] = url;
            } catch (MalformedURLException | URISyntaxException e) {
                System.err.println(e);
            }
        }

        libraries = urls;
        // libraries = new URL[0];
        System.out.println("WinResourceClassLoader initialized!");
    }
}