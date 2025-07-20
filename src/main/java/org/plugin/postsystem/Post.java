package org.plugin.postsystem;

import java.util.UUID;

public class Post {
    private final int id;
    private final UUID authorUUID;
    private final String authorName;
    private final String content;
    private final long timestamp;

    public Post(int id, UUID authorUUID, String authorName, String content, long timestamp) {
        this.id = id;
        this.authorUUID = authorUUID;
        this.authorName = authorName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public UUID getAuthorUUID() { return authorUUID; }
    public String getAuthorName() { return authorName; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
}