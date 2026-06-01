package com.PivotVC.demo.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class WorkingTree {

    private final Path repoRoot;

    public WorkingTree(Path repoRoot) {
        this.repoRoot = repoRoot;
    }

    public void writeFile(String relativePath, byte[] content)
            throws IOException {

        Path file = repoRoot.resolve(relativePath);

        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }

        Files.write(
                file,
                content,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public void deleteFile(String relativePath)
            throws IOException {

        Files.deleteIfExists(repoRoot.resolve(relativePath));
    }
}