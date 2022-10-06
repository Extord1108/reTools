package com.retools.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String avatar;
    private String password;
    private String wallpaper;
    private String email;
    private String phone;
    private Timestamp createDate;
    private String role;
}
