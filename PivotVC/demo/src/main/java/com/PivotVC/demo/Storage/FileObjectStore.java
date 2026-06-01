package com.PivotVC.demo.Storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    @Override
    public List<String> readLines(String sha)
            throws IOException {

        if (sha == null)
            return Collections.emptyList();

        String content =
                new String(
                        read(sha),
                        StandardCharsets.UTF_8
                );

        return Arrays.asList(content.split("\n", -1));
    }
}