package com.PivotVC.demo.Storage;

import org.springframework.stereotype.Component;

import java.io.IOException;

public interface ObjectStore {
    void write(String sha, byte[] content) throws IOException;
    byte[] read(String sha) throws IOException;
    boolean exists(String sha);

}

