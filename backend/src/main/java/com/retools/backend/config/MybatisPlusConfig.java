package com.retools.backend.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.retools.backend.mapper")
public class MybatisPlusConfig {
}
