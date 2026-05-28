package com.PivotVC.demo.Entities;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Commit {
    private String sha;
    private String treesha;
    private List<String> parentShas;
    private String message;
    private User author;
    private LocalDateTime timestamp;
    public Commit(String sha, String treeSha, List<String> parentShas,
                  String message, User author, LocalDateTime timestamp) {
        this.sha         = sha;
        this.treesha     = treeSha;
        this.message     = message;
        this.author      = author;
        this.timestamp   = timestamp != null ? timestamp : LocalDateTime.now();
        this.parentShas  = Collections.unmodifiableList(
                parentShas != null ? parentShas : List.of()
        );
    }
    public String getSha() {
        return sha;
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
}
