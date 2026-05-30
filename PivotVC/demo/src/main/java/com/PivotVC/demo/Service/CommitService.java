package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.*;
import com.PivotVC.demo.Storage.ObjectStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommitService {
    private final ShaUtils shaUtils;
    private final ObjectStore objectStore;
    public CommitService(ShaUtils shaUtils,ObjectStore objectStore){
        this.objectStore=objectStore;
        this.shaUtils=shaUtils;
    }
    public void commit(String message) throws IOException {
        //validating the user
        User author=Config.loadConfig();
        if(author==null){
            System.out.println("Please run: pivot config --username <name> --email <email>");
            return;
        }
        //this method first loads the index
        Index index=Index.load();
        HashMap<String,String> indexMap=index.getEntries();
        //now it traverses through all the files in this indexMap to get the sha for each of them
        //need to build a tree now :))
        List<TreeEntry> entries=new ArrayList<>();
        for(String filePath:indexMap.keySet()){
            String sha=indexMap.get(filePath);
            entries.add(new TreeEntry(EntryType.BLOB,sha,filePath));
        }
        TreeNode treeNode=new TreeNode(entries);
        //now we need to serialise the treeNode object in order to generate a sha for it and store it on the disk
        String serialisedContent=treeNode.serialize();
        byte[] treeBytes=serialisedContent.getBytes(StandardCharsets.UTF_8);
        //now we can generate a sha for it
        String sha=shaUtils.sha1Hex(serialisedContent);
        objectStore.write(sha,treeBytes);
        //now we just build and store the commit
        String parent=Head.loadHead();
        List<String> parentCommit=new ArrayList<>();
        parentCommit.add(parent);
        Commit commit=new Commit(sha,parentCommit,message,author, LocalDateTime.now());
    }
    public Commit getCommit(String sha) throws IOException {
        //we need to fetch the commit using its sha
        byte[] bytes=objectStore.read(sha);
        //now we need to deserialise this data into a commit object
        String commitData = new String(bytes, StandardCharsets.UTF_8);
        return Commit.deserialise(commitData,sha);
    }
    public List<Commit> log(){

    }
}
