package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.Commit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MergeService {
    public HashSet<String> collectAncestors(String shaA) throws IOException {
        HashSet<String> parentCommits=new HashSet<>();
        Queue<String> q=new LinkedList<>();
        q.add(shaA);
        while(!q.isEmpty()){
            String currCommit = q.poll();
            if(parentCommits.contains(currCommit)) continue;
            parentCommits.add(currCommit);
            Path commitPath= Path.of(".pivot","objects",currCommit);
            String commitContent= Files.readString(commitPath);
            Commit commit= Commit.deserialise(commitContent,currCommit);
            List<String> parCommits=commit.getParentShas();
            for(String parSha:parCommits){
                q.add(parSha);
            }
        }
        return parentCommits;
    }
    public String findCommonAncestor(String shaA,String shaB) throws IOException {
        HashSet<String> ancestors=collectAncestors(shaA);
        Queue<String> q=new LinkedList<>();
        String currSha=shaB;
        q.add(currSha);
        //we need to do a traversal unless we find a commit sha which is already there in teh set
        while(!q.isEmpty()){
            currSha=q.poll();
            if(ancestors.contains(currSha))return currSha;
            Path commitPath= Path.of(".pivot","objects",currSha);
            String commitContent= Files.readString(commitPath);
            Commit commit= Commit.deserialise(commitContent,currSha);
            List<String> parCommits=commit.getParentShas();
            for(String parSha:parCommits){
                q.add(parSha);
            }
        }
        return null;
    }
}
