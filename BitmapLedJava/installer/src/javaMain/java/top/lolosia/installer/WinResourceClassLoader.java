package top.lolosia.installer;

import org.jetbrains.annotations.Nullable;

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
        var fileName = name.replace('.', '/') + ".class";
        if (!resources.containsKey(fileName)) {
            return super.findClass(name);
        }
        byte[] classBytes = loadFromResource(fileName);

        return defineClass(name, classBytes, 0, classBytes.length);
    }


    @Override
    public InputStream getResourceAsStream(String name) {
        var name1 = name.startsWith("/") ? name.substring(1) : name;
        if (!resources.containsKey(name1)) {
            return super.getResourceAsStream(name);
        }
        var data = loadFromResource(name1);
        return new ByteArrayInputStream(data);
    }

    @Nullable
    @Override
    public URL getResource(String name) {
        // System.out.println("getResource: " + name);
        var name1 = name.startsWith("/") ? name.substring(1) : name;
        if (!resources.containsKey(name1)) {
            return super.getResource(name);
        }
        return resources.get(name1);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        var l = findResources0(name);
        var s = super.getResources(name);
        //noinspection unchecked
        return new CompoundEnumeration<URL>(new Enumeration[]{l, s});
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        var l = findResources0(name);
        var s = super.findResources(name);
        //noinspection unchecked
        return new CompoundEnumeration<URL>(new Enumeration[]{l, s});
    }

    private Enumeration<URL> findResources0(String name) {
        if (name.startsWith("/")) name = name.substring(1);
        List<URL> result = new ArrayList<>();

        for (Map.Entry<String, URL> entry : resources.entrySet()) {
            if (entry.getKey().equals(name)) {
                result.add(entry.getValue());
            }
        }
        return Collections.enumeration(result);
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
                            var path = getURL().getPath();
                            if (path.startsWith("/")) path = path.substring(1);
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
                        public InputStream getInputStream() throws FileNotFoundException {
                            if (data == null) connect();
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
                // System.out.println(name);
                var url = new URI("winres://IDR_JAR1/" + name).toURL();
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
                // System.out.println(lib);
                var url = new URI("file:/" + lib).toURL();
                urls[i] = url;
            } catch (MalformedURLException | URISyntaxException e) {
                System.err.println(e);
            }
        }

        libraries = urls;
        // libraries = new URL[0];
        // System.out.println("WinResourceClassLoader initialized!");
    }
}