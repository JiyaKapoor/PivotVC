package com.PivotVC.demo.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Branch {
    private String name;
    private String headCommitSha;
    public Branch(String name, String headCommitSha) {
        this.name          = Objects.requireNonNull(name, "name cannot be null");
        this.headCommitSha = headCommitSha != null ? headCommitSha : "";
    }
    public String getName() {
        return name;
    }

    public String getHeadCommitSha() {
        return headCommitSha;
    }
    public static void createBranch(String branchName) throws IOException {
        String currentSha=Head.loadHead();
        if(currentSha==null){
            System.out.println("No commits yet, cant create a branch");
            return;
        }
        Files.writeString(Path.of(".pivot","refs","heads",branchName),currentSha);
    }
}
