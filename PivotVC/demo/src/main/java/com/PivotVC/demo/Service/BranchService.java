package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.Branch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BranchService {
    //this service will be responsible for creating new branches
    //switching to a current branch
    //listing all the branches
    public void createBranch(String branchName) throws IOException {
        Branch.createBranch(branchName);
    }
    public List<Branch> listBranches() throws IOException{
        return Branch.getBranches();
    }
    //switching to a particular branch
    public void checkOut(String BranchName) throws IOException {
        //we basically need to update the HEAD file to contain the ref of this file
        Branch branch=Branch.loadBranch(BranchName);
        if(branch==null)System.out.println("No such branch exists");
        Files.writeString(Path.of(".pivot","HEAD"),Path.of(".pivot","refs","head",BranchName).toString());
        System.out.println("Switched to branch-"+ BranchName);
    }
}
