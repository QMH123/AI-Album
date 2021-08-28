package com.example.aiphoto_album.model;

public class Category {
    private Integer fileId;
    private String group;
    private String item;
    private String classifyId;

    public Category() {
    }

    public Category(Integer fileId, String group, String item) {
        this.fileId = fileId;
        this.group = group;
        this.item = item;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    @Override
    public String toString() {
        return "Category [classifyId=" + classifyId + ", fileId=" + fileId + ", group=" + group + ", item=" + item
                + "]";
    }
    
}
