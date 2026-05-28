package com.PivotVC.demo.Entities;

public class Blob {
    private String sha;
    private byte[] content;
    public Blob(String sha,byte[] content){
        this.sha=sha;
        this.content=content;
    }
    public String getSha() {
        return sha;
    }

    public byte[] getContent() {
        return content;
    }

}
