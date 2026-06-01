package com.PivotVC.demo.Controller;

import com.PivotVC.demo.Service.InitService;
import picocli.CommandLine.Command;

@Command(name = "init", description = "Initialize a repository")
public class InitCommand implements Runnable {

    @Override
    public void run() {
        try {
            InitService.initRepo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
