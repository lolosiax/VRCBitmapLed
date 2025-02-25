package top.lolosia.installer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WinResourcesPath implements Path {

    public WinResourcesPath(String path) {
        this.path = path;
    }

    private final String path;

    @NotNull
    @Override
    public FileSystem getFileSystem() {
        return WinResourcesFileSystemProvider.fileSystem;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Path getRoot() {
        return new WinResourcesPath("");
    }

    @Override
    public Path getFileName() {
        var str = path;
        if (str.endsWith("/")) str = str.substring(1);
        var array = str.split("/");
        str = array[str.length() - 1];
        return new WinResourcesPath(str);
    }

    @Override
    public Path getParent() {
        var str = path;
        if (str.endsWith("/")) str = str.substring(1);
        var index = str.lastIndexOf("/");
        str = str.substring(0, index + 1);
        return new WinResourcesPath(str);
    }

    @Override
    public int getNameCount() {
        return path.split("/").length;
    }

    @NotNull
    @Override
    public Path getName(int index) {
        return new WinResourcesPath(path.split("/")[index]);
    }

    @NotNull
    @Override
    public Path subpath(int beginIndex, int endIndex) {
        var list = path.split("/");
        var out = Arrays.asList(list).subList(beginIndex, endIndex);
        return new WinResourcesPath(String.join("/", out));
    }

    @Override
    public boolean startsWith(@NotNull Path other) {
        if (other instanceof WinResourcesPath) {
            return path.startsWith(((WinResourcesPath) other).path);
        }
        return false;
    }

    @Override
    public boolean endsWith(@NotNull Path other) {
        if (other instanceof WinResourcesPath) {
            return path.endsWith(((WinResourcesPath) other).path);
        }
        return false;
    }

    @NotNull
    @Override
    public Path normalize() {
        return this;
    }

    @NotNull
    @Override
    public Path resolve(@NotNull Path other) {
        if (!(other instanceof WinResourcesPath)) {
            throw new IllegalArgumentException("Cannot resolve " + other);
        }
        var otherPath = ((WinResourcesPath) other).path;
        var path = this.path;
        if (otherPath.startsWith("/")) otherPath = otherPath.substring(1);
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
        return new WinResourcesPath(path + "/" + otherPath);
    }

    @NotNull
    @Override
    public Path relativize(@NotNull Path other) {
        throw new UnsupportedOperationException("Cannot relativize " + other);
    }

    @NotNull
    @Override
    public URI toUri() {
        try {
            return new URI("winres://IDR_JAR1/" + path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public Path toAbsolutePath() {
        return this;
    }

    @NotNull
    @Override
    public Path toRealPath(@NotNull LinkOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public WatchKey register(@NotNull WatchService watcher, WatchEvent.Kind<?>[] events, @NotNull WatchEvent.Modifier... modifiers) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(@NotNull Path other) {
        if (other instanceof WinResourcesPath) {
            return path.compareTo(((WinResourcesPath) other).path);
        }
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof WinResourcesPath) {
            return path.equals(((WinResourcesPath) other).path);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @NotNull
    @Override
    public String toString() {
        return path;
    }
}
