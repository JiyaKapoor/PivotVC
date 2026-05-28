package com.PivotVC.demo.Entities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TreeNode {
    private String sha;
    private List<TreeEntry> entries;
    public TreeNode(String sha, List<TreeEntry> entries) {
        this.sha     = Objects.requireNonNull(sha, "sha cannot be null");
        this.entries = Collections.unmodifiableList(
                entries != null ? entries : List.of()
        );
    }
    public String getSha() {
        return sha;
    }

    public List<TreeEntry> getEntries() {
        return entries;
    }
}
