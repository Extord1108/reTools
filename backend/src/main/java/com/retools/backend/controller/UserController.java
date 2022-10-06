package com.retools.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.retools.backend.entity.User;
import com.retools.backend.mapper.UserMapper;
import com.retools.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject body) {
        User user = new User();
        user.setPassword(body.getString("password"));
        user.setUsername(body.getString("username"));
        user.setEmail(body.getString("email"));
        JSONObject jsonObject = new JSONObject();
        int ret = userService.register(user,body.getString("code"));
        if(ret == 1){
            jsonObject.put("code", 200);
            jsonObject.put("msg", "注册成功");
        }
        else if(ret == 2){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "该邮箱已被使用");
        }
        else if(ret == 3){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "邀请码不存在");
        }
        return jsonObject;
    }

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject body, HttpServletRequest request) {
        HttpSession session = request.getSession();

        JSONObject jsonObject = new JSONObject();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", body.getString("email"));
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "邮箱不存在");
        }
        else if(!user.getPassword().equals(body.getString("password"))){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "密码错误");
        }
        else{
            jsonObject.put("code", 200);
            jsonObject.put("msg", "登录成功");
            String avatar = user.getAvatar();
            //int wallpaperId = user.getWallpaperId();
            JSONObject userInfo = new JSONObject();
            userInfo.put("username", user.getUsername());
            userInfo.put("avatar", avatar);
            //userInfo.put("wallpaper", wallpaperId);
            jsonObject.put("userInfo", userInfo);
            session.setAttribute("user", user.getId());
            session.setMaxInactiveInterval(60 * 60 * 24 * 7);
        }
        return jsonObject;
    }

    @PostMapping("/logout")
    public JSONObject logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "退出成功");
        return jsonObject;
    }

    @GetMapping("/info")
    public JSONObject info(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        User user = userMapper.selectById(userId);
        if(user == null){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "用户不存在");
        }
        else{
            jsonObject.put("code", 200);
            jsonObject.put("msg", "获取用户信息成功");
            JSONObject userInfo = new JSONObject();
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("avatar",user.getAvatar());
            jsonObject.put("userInfo", userInfo);
        }
        return jsonObject;
    }

    @PostMapping("/update")
    public JSONObject update(@RequestBody JSONObject body, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        User user = userMapper.selectById(userId);
        if(user == null){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "用户不存在");
        }
        else{
            user.setUsername(body.getString("username"));
            user.setEmail(body.getString("email"));
            user.setPhone(body.getString("phone"));
            userMapper.updateById(user);
            jsonObject.put("code", 200);
            jsonObject.put("msg", "更新成功");
        }
        return jsonObject;
    }

    @PostMapping("/updatePassword")
    public JSONObject updatePassword(@RequestBody JSONObject body, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        User user = userMapper.selectById(userId);
        if(user == null){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "用户不存在");
        }
        else if(!user.getPassword().equals(body.getString("oldPassword"))){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "原密码错误");
        }
        else{
            user.setPassword(body.getString("newPassword"));
            userMapper.updateById(user);
            jsonObject.put("code", 200);
            jsonObject.put("msg", "更新成功");
        }
        return jsonObject;
    }
}
