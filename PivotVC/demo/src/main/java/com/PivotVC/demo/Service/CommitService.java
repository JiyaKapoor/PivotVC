package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.Commit;
import com.PivotVC.demo.Entities.ShaUtils;
import com.PivotVC.demo.Storage.ObjectStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CommitService {
    private final ShaUtils shaUtils;
    private final ObjectStore objectStore;
    public CommitService(ShaUtils shaUtils,ObjectStore objectStore){
        this.objectStore=objectStore;
        this.shaUtils=shaUtils;
    }
    public void commit(Commit commit) throws IOException {
        String serialisedCommit=commit.serialize();
        byte[] data = serialisedCommit.getBytes(StandardCharsets.UTF_8);
        String sha=shaUtils.sha1Hex(data);
        objectStore.write(sha,data);
    }
    public Commit getCommit(String sha) throws IOException {
        //we need to fetch the commit using its sha
        byte[] bytes=objectStore.read(sha);
        //now we need to deserialise this data into a commit object
        String commitData = new String(bytes, StandardCharsets.UTF_8);
        return Commit.deserialise(commitData,sha);
    }
}
