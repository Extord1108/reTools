package com.retools.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Image {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String path;
    private String type;
    private Integer uid;
}
