package com.example.aiphoto_album.model;

public class AlbumInfo {
    private Long fileId;
    private String title;
    private String imgUrl;
    private Integer number;
    
    public AlbumInfo() {
    }

    public AlbumInfo(Long fileId, String title, String imgUrl, Integer number) {
        this.fileId = fileId;
        this.title = title;
        this.imgUrl = imgUrl;
        this.number = number;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
    
}
