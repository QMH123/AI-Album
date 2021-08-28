package com.example.aiphoto_album.model;

public class Photo {
    private Integer fileId;
    private String fileName;
    private String imgUrl;
    private String userId;
    private String height;
    private String width;
    private String uploadTime;

    public Photo(Integer fileId, String fileName, String imgUrl, String userId, String height, String width,String uploadTime) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.imgUrl = imgUrl;
        this.userId = userId;
        this.height = height;
        this.width = width;
        this.uploadTime = uploadTime;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "Photo [fileId=" + fileId + ", fileName=" + fileName + ", height="
                + height + ", imgUrl=" + imgUrl + ", uploadTime=" + uploadTime
                + ", userId=" + userId + ", width=" + width + "]";
    }

}
