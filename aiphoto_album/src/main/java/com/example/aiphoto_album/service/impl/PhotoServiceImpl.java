package com.example.aiphoto_album.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.aiphoto_album.dao.CategoryMapper;
import com.example.aiphoto_album.dao.PhotoMapper;
import com.example.aiphoto_album.model.AlbumInfo;
import com.example.aiphoto_album.model.Category;
import com.example.aiphoto_album.model.Photo;
import com.example.aiphoto_album.model.PhotoInfo;
import com.example.aiphoto_album.service.PhotoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService{
    @Autowired
    PhotoMapper photoMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public int insertPhotoInfo(Photo photo) {
        return photoMapper.insertPhotoInfo(photo);
    }

    @Override
    public int insertClassifyInfo(Category category) {
        return photoMapper.insertClassifyInfo(category);
    }

    @Override
    public List<AlbumInfo> getAlbumInfo(Map<String, Object> map) {
        List<AlbumInfo> albumInfos = new ArrayList<AlbumInfo>();
        String[] items = categoryMapper.getAllItems(map);
        for(String item:items){
            AlbumInfo albumInfo = new AlbumInfo();
            map.put("item", item);
            Map<String,Object> cover = photoMapper.getFirstPhoto(map);
            //System.out.println(cover.get("fileId"));
            albumInfo.setFileId((Long)(cover.get("fileId")));
            albumInfo.setImgUrl((String)(cover.get("imgUrl")));
            albumInfo.setNumber(categoryMapper.getItemNum(map)); 
            albumInfo.setTitle(item);
            albumInfos.add(albumInfo);
        }
        return albumInfos;
    }

    @Override
    public PhotoInfo getPhotoInfo(Long fileId) {
        PhotoInfo photoInfo = new PhotoInfo();
        Map<String,String> map = photoMapper.getPhotoInfo(fileId);
        if (map == null){
            return null;
        }
        String[] categories = categoryMapper.getImgCategories(fileId);
        photoInfo.setCategories(categories);
        photoInfo.setFileId(fileId);
        photoInfo.setTitle(map.get("fileName"));
        photoInfo.setHeight(map.get("height"));
        photoInfo.setWidth(map.get("width"));
        photoInfo.setImgUrl(map.get("imgUrl"));
        photoInfo.setUploadTime(map.get("uploadTime"));
        
        return photoInfo;
    }

    @Override
    public Map<String, Object> getImgList(Map<String, Object> map) {
        List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
        imgList = photoMapper.getImgList(map);
        if(imgList.size() == 0){
            return null;
        }
        Integer total = categoryMapper.getItemNum(map);
        Integer pageSize = (Integer)map.get("pageSize");
        double pageNum = (double) total / pageSize;
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", (int)Math.ceil(pageNum));
        res.put("imgList", imgList);
        return res;
    }

    @Override
    public int deletePhotoInfo(Long fileId) {
        return photoMapper.deletePhotoInfo(fileId);
    }

    @Override
    public Map<String, Object> getAllImg(Map<String, Object> map) {
        List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
        imgList = photoMapper.getAllImg(map);
        if(imgList.size() == 0){
            return null;
        }
        Integer total = photoMapper.getPageNum((String)map.get("userId"));
        Integer pageSize = (Integer)map.get("pageSize");
        double pageNum = (double) total / pageSize;
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", (int)Math.ceil(pageNum));
        res.put("imgList", imgList);
        return res;
    }

    @Override
    public Map<String, Object> vagueSearch(Map<String, Object> map) {
        List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
        imgList = photoMapper.vagueSearch(map);
        if(imgList.size() == 0){
            return null;
        }
        Integer total = photoMapper.getSearchNum(map);
        Integer pageSize = (Integer)map.get("pageSize");
        double pageNum = (double) total / pageSize;
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", (int)Math.ceil(pageNum));
        res.put("imgList", imgList);
        return res;
    }

}
