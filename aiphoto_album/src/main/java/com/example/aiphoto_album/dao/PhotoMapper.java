package com.example.aiphoto_album.dao;

import java.util.List;
import java.util.Map;

import com.example.aiphoto_album.model.Category;
import com.example.aiphoto_album.model.Photo;

import org.springframework.stereotype.Repository;

@Repository
public interface PhotoMapper {
    int insertPhotoInfo(Photo photo);
    
    int insertClassifyInfo(Category category);

    int getAlbumNum(Map<String,String> map);

    Map<String,Object> getFirstPhoto(Map<String,Object> map);

    Map<String,String> getPhotoInfo(Long fileId);

    List<Map<String,Object>> getImgList(Map<String,Object> map);

    List<Map<String,Object>> getAllImg(Map<String,Object> map);

    int deletePhotoInfo(Long fileId);

    int getPageNum(String userId);

    List<Map<String,Object>> vagueSearch(Map<String,Object> map);
    
    int getSearchNum(Map<String,Object> map);
}
