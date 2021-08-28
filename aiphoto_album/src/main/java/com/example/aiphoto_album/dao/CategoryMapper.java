package com.example.aiphoto_album.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper {
    String[] getAllItems(Map<String,Object> map);
    
    Integer getItemNum(Map<String,Object> map);

    String[] getImgCategories(Long fileId);
}
