package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.Index;
import com.PivotVC.demo.Entities.ShaUtils;
import com.PivotVC.demo.Storage.FileObjectStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StagingAreaService {
    private FileObjectStore objectStore;
    private Index index;
    private ShaUtils shaUtil;
    public StagingAreaService(FileObjectStore fileObjectStore,Index index,ShaUtils utils) throws IOException {
        this.objectStore=fileObjectStore;
        this.index=Index.load();
    }
    public void stageFile(Path filePath) throws IOException {
        //step 1 is to generate sha for it
        byte[] bytes= Files.readAllBytes(filePath);
        String sha=shaUtil.sha1Hex(bytes);
        index.addEntry(filePath.toString(), sha);
        index.save();
    }
    public void clearStagingArea() throws IOException {
        index.clearMap();
    }
    public Index loadIndex() throws IOException {
        return Index.load();
    }
}
