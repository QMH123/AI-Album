package com.example.aiphoto_album.model;

public class PhotoInfo {
    private Long fileId;
    private String title;
    private String imgUrl;
    private String height;
    private String width;
    private String uploadTime;
    private String[] categories;

    public PhotoInfo() {
    }

    public PhotoInfo(Long fileId, String title, String imgUrl, String height, String width, String uploadTime,
            String[] categories) {
        this.fileId = fileId;
        this.title = title;
        this.imgUrl = imgUrl;
        this.height = height;
        this.width = width;
        this.uploadTime = uploadTime;
        this.categories = categories;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }
    
    
}
