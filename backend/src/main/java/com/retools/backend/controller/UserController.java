package com.retools.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.retools.backend.entity.User;
import com.retools.backend.mapper.UserMapper;
import com.retools.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        user.setUsername(body.getString("username"));
        user.setPassword(body.getString("password"));
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
            jsonObject.put("msg", "该手机号已被使用");
        }
        else if(ret == 4){
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
        queryWrapper.eq("password", body.getString("password"));
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "用户名或密码错误");
        }
        else{
            jsonObject.put("code", 200);
            jsonObject.put("msg", "登录成功");
            jsonObject.put("user", user);
            session.setAttribute("user", user);
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
}