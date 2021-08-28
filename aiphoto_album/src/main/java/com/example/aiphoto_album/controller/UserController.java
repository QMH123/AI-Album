package com.example.aiphoto_album.controller;

import com.example.aiphoto_album.api.CommonResult;
import com.example.aiphoto_album.model.User;
import com.example.aiphoto_album.service.UserService;
import com.example.aiphoto_album.util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "UserController", description = "用户管理")
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "查询用户是否存在")
    @GetMapping("/searchUserInfo")
    @ResponseBody
    public CommonResult<User> searchUserInfo(@RequestParam(value = "userId", required = true) String userId){
        User user = userService.searchUserInfo(userId);
        //System.out.println(user.toString());
        if (user == null){
            return CommonResult.failed("用户不存在！");
        }
        return CommonResult.success(user);
    }

    @ApiOperation(value = "用户登陆")
    @GetMapping("/login")
    @ResponseBody
    public CommonResult<Object> userLogin(@RequestParam(value = "userId", required = true) String userId,
    @RequestParam(value = "password", required = true) String password){
        User user = userService.searchUserInfo(userId);
        if (user == null){
            return CommonResult.failed("用户不存在！");
        }
        if (user.getPassword().equals(password)){
            String token = jwtTokenUtil.generateToken(user);
            return CommonResult.success(token, "登陆成功");
        } else{
            return CommonResult.failed("密码错误");
        }
    }
}
