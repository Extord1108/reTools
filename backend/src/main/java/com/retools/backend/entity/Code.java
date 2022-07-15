package com.retools.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Code {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String code;
}
