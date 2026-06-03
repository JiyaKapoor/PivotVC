package com.example.PivotVC_Web.Entities;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Head {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefType refType;
    @Column(name = "repo_id", nullable = false, unique = true)
    private Long repoId;
    @Column(name = "branch_name")
    private String branchName;
    @Column(name = "commit_sha")
    private String commitSha; //in case it is detached
    public Head(){

    }
    public Head(Long repoId,String branchName){
        this.repoId=repoId;
        this.branchName=branchName;
    }
}
