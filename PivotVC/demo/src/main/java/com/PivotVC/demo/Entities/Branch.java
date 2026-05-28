package com.PivotVC.demo.Entities;

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
}
