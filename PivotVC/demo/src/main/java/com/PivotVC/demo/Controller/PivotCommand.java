package com.PivotVC.demo.Controller;


import picocli.CommandLine.Command;

@Command(
        name = "pivot",
        subcommands = {
                InitCommand.class
        }
)
public class PivotCommand {
}