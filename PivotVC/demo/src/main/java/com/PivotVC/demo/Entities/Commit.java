package com.PivotVC.demo.Entities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commit {
    private String treesha;
    private List<String> parentShas;
    private String message;
    private User author;
    private LocalDateTime timestamp;
    public Commit(String treeSha, List<String> parentShas,
                  String message, User author, LocalDateTime timestamp) {
        this.treesha     = treeSha;
        this.message     = message;
        this.author      = author;
        this.timestamp   = timestamp != null ? timestamp : LocalDateTime.now();
        this.parentShas  = Collections.unmodifiableList(
                parentShas != null ? parentShas : List.of()
        );
    }

    public String getTreeSha() {
        return treesha;
    }

    public List<String> getParentShas() {
        return parentShas;
    }

    public String getMessage() {
        return message;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("tree ").append(treesha).append("\n");
        for (String parent : parentShas) {
            sb.append("parent ").append(parent).append("\n");
        }
        sb.append("author ").append(author.getName())
                .append(" <").append(author.getEmailId()).append(">")
                .append("\n");
        sb.append("date ").append(timestamp.toString()).append("\n");
        sb.append("message ").append(message).append("\n");
        return sb.toString();
    }
    public static Commit deserialise(String data,String sha){
        String treeSha=null;
        List<String> parentShas=new ArrayList<>();
        String message=null;
        User author=null;
        LocalDateTime timestamp=null;
        for (String line : data.split("\n")) {
            if (line.startsWith("tree "))         treeSha   = line.substring(5).trim();
            else if (line.startsWith("parent "))  parentShas.add(line.substring(7).trim());
            else if (line.startsWith("author ")) {
                String value = line.substring(7).trim();
                String username = value.substring(0, value.indexOf("<")).trim();
                String email = value.substring(value.indexOf("<") + 1, value.indexOf(">")).trim();
                author = new User(username, email);
            }
            else if (line.startsWith("date "))    timestamp = LocalDateTime.parse(line.substring(5).trim());
            else if (line.startsWith("message ")) message   = line.substring(8).trim();
        }

        return new Commit(treeSha, parentShas, message, author, timestamp);
    }
}
