package com.PivotVC.demo.Controller;

import com.PivotVC.demo.Entities.Config;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;

@Command(name = "config", description = "Configure username and email")
public class ConfigCommand implements Runnable {

    @Option(names = "--username", required = true,
            description = "User name")
    private String username;

    @Option(names = "--email", required = true,
            description = "User email")
    private String email;

    @Override
    public void run() {
        try {
            Config.saveConfig(username,email);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}