package com.example.aiphoto_album.service;

import java.util.List;
import java.util.Map;

import com.example.aiphoto_album.model.AlbumInfo;
import com.example.aiphoto_album.model.Category;
import com.example.aiphoto_album.model.Photo;
import com.example.aiphoto_album.model.PhotoInfo;

public interface PhotoService {
    int insertPhotoInfo(Photo photo);

    int insertClassifyInfo(Category category);

    List<AlbumInfo> getAlbumInfo(Map<String,Object> map);

    PhotoInfo getPhotoInfo(Long fileId);

    Map<String,Object> getImgList(Map<String,Object> map);

    Map<String,Object> getAllImg(Map<String,Object> map);

    Map<String,Object> vagueSearch(Map<String,Object> map);

    int deletePhotoInfo(Long fileId);

}
