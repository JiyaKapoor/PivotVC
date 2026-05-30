package com.PivotVC.demo.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Head {
    private static final Path head_path=Path.of(".pivot","head");
    public static String loadHead() throws IOException {
        if(!Files.exists(head_path))return null;
        return Files.readString(head_path);
    }
    public static void updateHead(String updatedSha) throws IOException {
        Files.createDirectories(head_path.getParent());
        Files.writeString(head_path,updatedSha);

    }
}
