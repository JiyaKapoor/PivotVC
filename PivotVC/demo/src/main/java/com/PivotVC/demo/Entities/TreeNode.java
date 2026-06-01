package com.PivotVC.demo.Entities;

import java.util.*;

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
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (TreeEntry entry : this.getEntries()) {
            map.put(entry.getName(), entry.getSha()); // name is already the full path
        }
        return map;
    }

}
