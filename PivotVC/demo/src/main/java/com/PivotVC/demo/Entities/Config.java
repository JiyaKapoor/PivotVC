package com.PivotVC.demo.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Path config_path=Path.of("pivot","config");

    public static User loadConfig() throws IOException{
        if(!Files.exists(config_path))return null;
        String userData=Files.readString(config_path);
        return User.deserialise(userData);
    }
    public static void saveConfig(String username,String emailId) throws  IOException{
        Files.createDirectories(config_path.getParent());
        StringBuilder sb=new StringBuilder();
        sb.append("username=").append(username).append("\n")
                .append("email=").append(emailId).append("\n");
        Files.writeString(config_path,sb.toString());
    }
}
