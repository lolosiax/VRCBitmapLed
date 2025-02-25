package top.lolosia.installer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Set;

public class WinResourcesFileSystem extends FileSystem {
    @Override
    public FileSystemProvider provider() {
        return WinResourcesFileSystemProvider.instance;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public String getSeparator() {
        return "/";
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        return Collections.emptyList();
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return Set.of();
    }

    @NotNull
    @Override
    public Path getPath(@NotNull String first, @NotNull String... more) {
        for (int i = 0; i < more.length; i++) {
            var str = more[i];
            if (str.startsWith("/")) str = str.substring(1);
            if (str.endsWith("/")) str = str.substring(0, str.length() - 1);
            more[i] = str;
        }
        if (first.endsWith("/")) first = first.substring(0, first.length() - 1);
        return new WinResourcesPath(first + "/" + String.join("/", more));
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException();
    }

    @Override
    public WatchService newWatchService() throws IOException {
        throw new UnsupportedOperationException();
    }
}
