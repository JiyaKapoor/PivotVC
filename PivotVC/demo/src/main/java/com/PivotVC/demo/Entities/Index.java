package com.PivotVC.demo.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Index {
    private static final Path index_path=Path.of(".pivot","index");
    private HashMap<String,String> indexMap=new LinkedHashMap<>();
    public void addEntry(String filepath,String sha){
        indexMap.put(filepath,sha);
    }
    public void clearMap(){
        indexMap.clear();
    }
    public HashMap<String,String> getEntries(){
        return this.indexMap;
    }
    public void save() throws IOException {
        //basically we are trying to save the index map to the disk
        Files.createDirectories(index_path.getParent());
        StringBuilder sb = new StringBuilder();
        indexMap.forEach((path, sha) ->
                sb.append(sha).append("  ").append(path).append("\n")
        );
        Files.writeString(index_path, sb.toString());
    }
    public static Index load() throws IOException {
        Index index = new Index();
        if (!Files.exists(index_path)) return index;
        Files.lines(index_path).forEach(line -> {
            String[] parts = line.split("  ", 2);
            if (parts.length == 2) index.addEntry(parts[1], parts[0]);
        });
        return index;
    }
}
