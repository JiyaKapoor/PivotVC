package com.PivotVC.demo.Storage;


import java.io.IOException;
import java.util.List;

public interface ObjectStore {
    void write(String sha, byte[] content) throws IOException;
    byte[] read(String sha) throws IOException;
    boolean exists(String sha);
    List<String> readLines(String sha) throws IOException;

}

