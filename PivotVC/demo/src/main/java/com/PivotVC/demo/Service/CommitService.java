package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.*;
import com.PivotVC.demo.Storage.FileObjectStore;
import com.PivotVC.demo.Storage.ObjectStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

import static com.PivotVC.demo.Service.TreeService.buildTree;

public class CommitService {
    private final ShaUtils shaUtils;
    private final ObjectStore objectStore;
    public CommitService(ShaUtils shaUtils,ObjectStore objectStore){
        this.objectStore=objectStore;
        this.shaUtils=shaUtils;
    }
    public static void commit(String message) throws IOException {
        //validating the user
        User author=Config.loadConfig();
        if(author==null){
            System.out.println("Please run: pivot config --username <name> --email <email>");
            return;
        }
        //now we need to serialise the treeNode object in order to generate a sha for it and store it on the disk
        TreeNode node=buildTree();
        String serialisedContent=node.serialize();
        String sha=ShaUtils.sha1Hex(serialisedContent);
        byte[] treeBytes=serialisedContent.getBytes();
        FileObjectStore fileObjectStore=new FileObjectStore();
        fileObjectStore.write(sha,treeBytes);
        //now we just build and store the commit
        String parent=Head.loadHead();
        List<String> parentCommit=new ArrayList<>();
        if(parent!=null)parentCommit.add(parent);
        Commit commit=new Commit(sha,parentCommit,message,author, LocalDateTime.now());
        String serialisedCommit = commit.serialize();

        String commitSha=ShaUtils.sha1Hex(serialisedCommit);
        //saving the commit object on the disk
        fileObjectStore.write(commitSha,serialisedCommit.getBytes(StandardCharsets.UTF_8));
        //update the head
        Head.updateHead(commitSha);
    }
    public Commit getCommit(String sha) throws IOException {
        //we need to fetch the commit using its sha
        byte[] bytes=objectStore.read(sha);
        //now we need to deserialise this data into a commit object
        String commitData = new String(bytes, StandardCharsets.UTF_8);
        return Commit.deserialise(commitData,sha);
    }
    public List<Commit> log() throws IOException {
        //we first need to go to the current branch that we are on
        List<Commit> commitLog=new ArrayList<>();
        Queue<String> q=new LinkedList<>();
        Set<String> vis=new HashSet<>();
        String currCommit=Head.loadHead();
        q.add(currCommit);
        vis.add(currCommit);
        //traversal
        while(!q.isEmpty()){
            String latestCommit=q.poll();
            Path commitPath=Path.of(".pivot","objects",latestCommit);
            String commitContent= Files.readString(commitPath);
            Commit commit=Commit.deserialise(commitContent,latestCommit);
            commitLog.add(commit);
            List<String> parentShas=commit.getParentShas();
            for(String parentCommit:parentShas){
                if(!vis.contains(parentCommit)){
                    q.add(parentCommit);
                    vis.add(parentCommit);
                }
            }
        }
        return commitLog;
    }
}
