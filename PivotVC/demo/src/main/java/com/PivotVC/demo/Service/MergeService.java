package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.Commit;
import com.PivotVC.demo.Entities.ShaComparisonResult;
import com.PivotVC.demo.Entities.TreeNode;
import com.PivotVC.demo.Entities.WorkingTree;
import com.PivotVC.demo.Storage.ObjectStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class MergeService {
    public ObjectStore objectStore;
    public WorkingTree workingTree;
    public MergeService(ObjectStore objectStore,WorkingTree workingTree){
        this.objectStore=objectStore;
        this.workingTree=workingTree;
    }
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
    //now we need to implement three-way merge
    public void threeWayMerge(String shaA,String shaB) throws IOException {
        String baseSha=findCommonAncestor(shaA,shaB);
        TreeNode baseTree=TreeService.buildTree(baseSha);
        TreeNode sourceTree=TreeService.buildTree(shaA);
        TreeNode targetTree=TreeService.buildTree(shaB);
        //now we need to compare each file across all the tree
        Map<String, String> baseMap   = baseTree.toMap();
        Map<String, String> sourceMap = sourceTree.toMap();
        Map<String, String> targetMap = targetTree.toMap();
        Set<String> allPaths=new HashSet<>();
        allPaths.addAll(baseMap.keySet());
        allPaths.addAll(sourceMap.keySet());
        allPaths.addAll(targetMap.keySet());
        ShaComparisonResult result=compareBySha(allPaths,baseMap,sourceMap,targetMap);
        for (Map.Entry<String, String> e : result.resolved.entrySet()) {
            byte[] content = objectStore.read(e.getValue());
            workingTree.writeFile(e.getKey(), content);
        }
        for(Map.Entry<String,String> e: result.deleted.entrySet()){
            workingTree.deleteFile(e.getKey());
        }
        List<String> conflicts = new ArrayList<>();

        for (String path : result.needsLineMerge) {
            boolean conflicted =
                    lineLevelMerge(
                            path,
                            baseMap.get(path),
                            sourceMap.get(path),
                            targetMap.get(path)
                    );

            if (conflicted) {
                conflicts.add(path);
            }
        }

        if (!conflicts.isEmpty()) {
            System.out.println("Merge completed with conflicts:");
            conflicts.forEach(System.out::println);
        }
    }
    private ShaComparisonResult compareBySha(Set<String> allPaths,
                                             Map<String, String> baseMap,
                                             Map<String, String> sourceMap,
                                             Map<String, String> targetMap) {
        Map<String, String> resolved= new HashMap<>();
        Map<String, String> deleted= new HashMap<>();
        Set<String> needsLineMerge= new HashSet<>();

        for (String path : allPaths) {
            String base   = baseMap.get(path);
            String source = sourceMap.get(path);
            String target = targetMap.get(path);

            // both sides same → take it, no merge needed
            if (Objects.equals(source, target)) {
                if (source == null) deleted.put(path, null);
                else resolved.put(path, source);

                // only target changed
            } else if (Objects.equals(base, source)) {
                if (target == null) deleted.put(path, null);
                else resolved.put(path, target);

                // only source changed
            } else if (Objects.equals(base, target)) {
                if (source == null) deleted.put(path, null);
                else resolved.put(path, source);

                // both sides modified differently → needs line level merge
            } else {
                needsLineMerge.add(path);
            }
        }

        return new ShaComparisonResult(resolved, deleted, needsLineMerge);
    }
    private boolean lineLevelMerge(
            String path,
            String baseSha,
            String sourceSha,
            String targetSha) throws IOException {

        List<String> base =
                objectStore.readLines(baseSha);

        List<String> source =
                objectStore.readLines(sourceSha);

        List<String> target =
                objectStore.readLines(targetSha);

        StringBuilder merged = new StringBuilder();

        boolean conflict = false;

        int max =
                Math.max(
                        base.size(),
                        Math.max(source.size(), target.size())
                );

        for (int i = 0; i < max; i++) {

            String b = getLine(base, i);
            String s = getLine(source, i);
            String t = getLine(target, i);

            if (Objects.equals(s, t)) {
                merged.append(s == null ? "" : s).append("\n");
            }

            else if (Objects.equals(b, s)) {
                merged.append(t == null ? "" : t).append("\n");
            }

            else if (Objects.equals(b, t)) {
                merged.append(s == null ? "" : s).append("\n");
            }

            else {

                conflict = true;

                merged.append("<<<<<<< SOURCE\n");

                if (s != null)
                    merged.append(s).append("\n");

                merged.append("=======\n");

                if (t != null)
                    merged.append(t).append("\n");

                merged.append(">>>>>>> TARGET\n");
            }
        }

        workingTree.writeFile(
                path,
                merged.toString().getBytes(StandardCharsets.UTF_8)
        );

        return conflict;
    }
    private String getLine(List<String> lines, int idx) {
        return idx < lines.size() ? lines.get(idx) : null;
    }

}
