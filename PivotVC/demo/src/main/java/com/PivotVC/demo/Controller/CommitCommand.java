package com.PivotVC.demo.Controller;


import com.PivotVC.demo.Service.CommitService;
import com.PivotVC.demo.Service.InitService;
import picocli.CommandLine;

@CommandLine.Command(name = "commit", description = "commit changes")
public class CommitCommand implements Runnable {
    @CommandLine.Parameters(index="0")
    private String message;
    @Override
    public void run() {
        try {
            CommitService.commit(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}