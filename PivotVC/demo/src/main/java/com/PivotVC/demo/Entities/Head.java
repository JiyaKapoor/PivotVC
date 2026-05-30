package com.PivotVC.demo.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Head {
    private static final Path head_path=Path.of(".pivot","head");
    public static String loadHead() throws IOException {
        //a tiny update-> head should store reference to the current branch we are on
        if(!Files.exists(head_path))return null;
        String content=Files.readString(head_path).trim();
        if(content.startsWith("refs/")){
            Path branchPath=Path.of(".pivot",content);
            if(!Files.exists(branchPath))return null;
            return Files.readString(branchPath).trim();
        }
        return content; //detached head case where in the head stores the sha
    }
    public static void updateHead(String updatedSha) throws IOException {
        String content=Files.readString(head_path).trim();
        if(content.startsWith("ref/")){
            Path branchPath=Path.of(".pivot",content);
            Files.createDirectories(branchPath.getParent());
            Files.writeString(branchPath,updatedSha);
        }
        else{
            Files.writeString(head_path, updatedSha);
        }
    }
}
