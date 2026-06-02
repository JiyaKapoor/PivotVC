package com.PivotVC.demo.Service;

import com.PivotVC.demo.Entities.Index;
import com.PivotVC.demo.Entities.ShaUtils;
import com.PivotVC.demo.Storage.FileObjectStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StagingAreaService {
    private FileObjectStore objectStore;

    public static void stageFile(Path filePath) throws IOException {
        //step 1 is to generate sha for it
        byte[] bytes= Files.readAllBytes(filePath);
        String sha=ShaUtils.sha1Hex(bytes);
        Index index=Index.load();
        Path relative = Paths.get("")
                .toAbsolutePath()
                .relativize(filePath.toAbsolutePath());

        index.addEntry(relative.toString(), sha);
        index.save();
    }
    public void clearStagingArea() throws IOException {
        Index index=loadIndex();
        index.clearMap();
    }
    public Index loadIndex() throws IOException {
        return Index.load();
    }
}
