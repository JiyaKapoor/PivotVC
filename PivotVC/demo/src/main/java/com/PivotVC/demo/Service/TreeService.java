package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TreeService {
    public static TreeNode buildTree() throws IOException {
        //the tree must contain all the staged files as well as the files currently in the commit tree
        Index index=Index.load();
        HashMap<String,String> indexMap=index.getEntries();
        HashSet<TreeEntry> entries=new LinkedHashSet<>();
        String head = Files.readString(Path.of(".pivot", "HEAD")).trim();
        String branchName = head.substring(head.lastIndexOf('/') + 1);
        // refs/head/
        String latestCommit=Files.readString(Path.of(".pivot","refs","heads",branchName)).trim();
        for(String filePath:indexMap.keySet()){
            String sha=indexMap.get(filePath);
            entries.add(new TreeEntry(EntryType.BLOB,sha,filePath));
        }
        index.clearMap();
        //there are already files in the branch
        if(!latestCommit.isBlank()){
            Commit commit=Commit.deserialise(Files.readString(Path.of(".pivot","objects",latestCommit)),latestCommit);
            String treeSha=commit.getTreeSha();
            HashMap<String,String> currTree=new HashMap<>();
            flattenTree(treeSha,"",currTree);
            for(String filePath:currTree.keySet()){
                String sha=currTree.get(filePath);
                entries.add(new TreeEntry(EntryType.BLOB,sha,filePath));
            }
        }
        TreeNode node=new TreeNode(entries);
        return node;
    }
    public static TreeNode buildTree(String currCommit) throws IOException {
        Commit commit=Commit.deserialise(Files.readString(Path.of(".pivot","objects",currCommit)),currCommit);
        String treeSha=commit.getTreeSha();
        HashSet<TreeEntry> entries=new LinkedHashSet<>();
        HashMap<String,String> currTree=new HashMap<>();
        flattenTree(treeSha,"",currTree);
        for(String filePath:currTree.keySet()){
            String sha=currTree.get(filePath);
            entries.add(new TreeEntry(EntryType.BLOB,sha,filePath));
        }
        return new TreeNode(entries);
    }
    public static void flattenTree(String treeSha,String currPath,HashMap<String,String> map) throws IOException {
        Path treePath = Path.of(".pivot", "objects", treeSha);
        String content = Files.readString(treePath);
        for(String line:content.split("\n")){
            if(line.isBlank())continue;
            String[] parts=line.split(" ",3);
            String type=parts[0];
            String sha=parts[1];
            String filename=parts[2];
            String fullPath = currPath.isEmpty() ? filename : currPath + "/" + filename;
            if(type.equals("blob")){
                map.put(fullPath,sha);
            }
            else{
                flattenTree(sha,fullPath,map);
            }
        }
    }
}
