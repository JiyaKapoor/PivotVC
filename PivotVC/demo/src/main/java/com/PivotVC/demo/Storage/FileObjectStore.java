package com.PivotVC.demo.Storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileObjectStore implements ObjectStore {

    @Override
    public void write(String sha, byte[] content) throws IOException {
        Path path = Paths.get(".pivot", "objects", sha);
        Files.createDirectories(path.getParent());
        Files.write(path, content);
    }

    @Override
    public byte[] read(String sha) throws IOException {
        Path path = Paths.get(".pivot", "objects", sha);
        return Files.readAllBytes(path);
    }

    @Override
    public boolean exists(String sha) {
        return Files.exists(Paths.get(".pivot", "objects", sha));
    }
}