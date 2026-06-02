package com.PivotVC.demo.Controller;

import com.PivotVC.demo.Service.InitService;
import com.PivotVC.demo.Service.StagingAreaService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;

@Command(name = "add", description = "adds file to the staging area")
public class addCommand implements Runnable {
    @Parameters(index="0")
    private Path filePath;
    @Override
    public void run() {
        try {
            StagingAreaService.stageFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}