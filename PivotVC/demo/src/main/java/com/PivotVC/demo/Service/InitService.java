package com.PivotVC.demo.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InitService {
    public static void initRepo() throws IOException {
        Files.createDirectories(Path.of(".pivot","objects"));
        Files.createDirectories(Path.of(".pivot", "refs", "heads"));
        // initial HEAD pointing to main branch
        Path headPath = Path.of(".pivot", "HEAD");
        if(!Files.exists(headPath)) {
            Files.writeString(headPath, "ref: refs/heads/main\n");
        }
        System.out.println("Initialized empty repository in .pivot/");
    }
}
