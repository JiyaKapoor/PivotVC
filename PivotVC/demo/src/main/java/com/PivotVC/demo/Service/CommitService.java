package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.Commit;
import com.PivotVC.demo.Entities.ShaUtils;
import com.PivotVC.demo.Storage.ObjectStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CommitService {
    @Autowired
    private ShaUtils shaUtils;
    @Autowired
    private ObjectStore objectStore;
    public void commit(Commit commit) throws IOException {
        String serialisedCommit=commit.serialize();
        byte[] data = serialisedCommit.getBytes(StandardCharsets.UTF_8);
        String sha=shaUtils.sha1Hex(data);
        objectStore.write(sha,data);
    }
}
