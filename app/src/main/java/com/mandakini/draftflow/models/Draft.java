package com.mandakini.draftflow.models;

public class Draft {

    private int id;
    private String title;
    private String content;
    private String imagePath;
    private String createdAt;
    private String updatedAt;
    private String syncStatus;

    public Draft() {
    }

    public Draft(String title, String content, String imagePath, String createdAt, String updatedAt, String syncStatus) {
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.syncStatus = syncStatus;
    }

    public Draft(int id, String title, String content, String imagePath, String createdAt, String updatedAt, String syncStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.syncStatus = syncStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
}