package com.PivotVC.demo.Entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class TreeNode {
    private HashSet<TreeEntry> entries;
    public TreeNode(HashSet<TreeEntry> entries) {
        this.entries = entries;
    }

    public HashSet<TreeEntry> getEntries() {
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
