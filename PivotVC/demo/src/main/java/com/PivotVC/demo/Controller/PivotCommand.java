package com.PivotVC.demo.Controller;


import picocli.CommandLine.Command;

@Command(
        name = "pivot",
        subcommands = {
                InitCommand.class,
                addCommand.class,
                CommitCommand.class,
                ConfigCommand.class
        }
)
public class PivotCommand {
}