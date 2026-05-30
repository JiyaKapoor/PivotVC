package com.PivotVC.demo.Entities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TreeNode {
    private List<TreeEntry> entries;
    public TreeNode(List<TreeEntry> entries) {
        this.entries = Collections.unmodifiableList(
                entries != null ? entries : List.of()
        );
    }

    public List<TreeEntry> getEntries() {
        return entries;
    }
    public String serialize(){
        StringBuilder sb=new StringBuilder();
        for(TreeEntry treeEntry:entries){
            sb.append(treeEntry.getType()).append(" ")
                    .append(treeEntry.getName()).append(" ")
                    .append(treeEntry.getSha()).append("/n");
        }
        return sb.toString();
    }
}
