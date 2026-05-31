package com.PivotVC.demo.Entities;

import java.util.Objects;

public class TreeEntry {
    private EntryType type;
    private String sha;
    private String name;
    public TreeEntry(EntryType type, String sha, String name) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.sha  = Objects.requireNonNull(sha,  "sha cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }
    public EntryType getType() { return type; }
    public String getSha()     { return sha; }
    public String getName()    { return name; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeEntry)) return false;
        TreeEntry e = (TreeEntry) o;
        return Objects.equals(name, e.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
