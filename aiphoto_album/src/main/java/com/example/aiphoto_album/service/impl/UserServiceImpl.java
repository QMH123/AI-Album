package com.example.aiphoto_album.service.impl;

import com.example.aiphoto_album.dao.UserMapper;
import com.example.aiphoto_album.model.User;
import com.example.aiphoto_album.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public User searchUserInfo(String userId) {
        return userMapper.searchUserInfo(userId);
    }
    
}
