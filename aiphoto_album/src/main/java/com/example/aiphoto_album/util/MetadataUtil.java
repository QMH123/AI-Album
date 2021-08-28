package com.example.aiphoto_album.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


public class MetadataUtil {
    public Map<String, String> getPhotoTime(File file) throws ImageProcessingException, IOException{
        Map<String, String> map = new HashMap<>();
		// return map;
        Metadata metadata = ImageMetadataReader.readMetadata(file);  
         for (Directory directory : metadata.getDirectories()) {  
             for (Tag tag : directory.getTags()) {  
                 String tagName = tag.getTagName();  //标签名
                 String desc = tag.getDescription(); //标签信息
                 if (tagName.equals("Image Height")) {  
                     //System.out.println("图片高度: "+desc);
                     map.put("height", desc);
                 } else if (tagName.equals("Image Width")) {  
                     map.put("width", desc);
                 } else if (tagName.equals("Date/Time")) {  
                     System.out.println("拍摄时间: "+desc);
                     map.put("date", desc);
                 }else if (tagName.equals("File Modified Date")) { 
                    //System.out.println("最后修改时间: "+desc);
                    String[] split = desc.split(" ");
                    String year = split[5];
                    String month = split[1].substring(0,split[1].length()-1);
                    month = month.length()>1?month:'0'+month;
                    String day = split[2];
                    day = day.length()>1?day:'0'+day;
                    String time = split[3];
                    String date = year+'-'+month+'-'+day+' '+time;
                    //System.out.println(date);
                    map.put("date", date);
                 }
                
                
             }  
         }  
         return map;
    }
}
