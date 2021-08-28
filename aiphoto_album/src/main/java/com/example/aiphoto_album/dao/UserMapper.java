package com.example.aiphoto_album.dao;

import com.example.aiphoto_album.model.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User searchUserInfo(String userId);
}
