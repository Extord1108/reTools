package com.retools.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.retools.backend.entity.Image;
import com.retools.backend.mapper.ImageMapper;
import com.retools.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
public class ImageController {
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("image/upload")
    public JSONObject uploadImage(@RequestParam("file")MultipartFile file,@RequestParam("type") String type, HttpServletRequest request) throws IOException {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("user");
        //重命名文件
        String path = ResourceUtils.getURL("classpath:").getPath()+"static/image/";
        path = URLDecoder.decode(path,"UTF-8");
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));
        fileName = UUID.randomUUID()+suffixName;
        //检查后缀
        Set allowSuffix = new HashSet<>(Arrays.asList(".jpg",".jpeg",".png",".webp"));
        if(!allowSuffix.contains(suffixName)){
            jsonObject.put("code", 400);
            jsonObject.put("msg", "文件类型错误");
            return jsonObject;
        }

        //尝试IO
        File tempFile = new File(path+fileName);
        try{
            tempFile.createNewFile();
            file.transferTo(tempFile);
            try{
                System.out.println(tempFile.getPath());
                BufferedImage image = ImageIO.read(tempFile);
                System.out.println(image.getHeight());
                image.getHeight();
                image.getWidth();
            }catch (IOException e){
                jsonObject.put("code", 400);
                jsonObject.put("msg", "文件读取失败");
                tempFile.delete();
                return jsonObject;
            }
            //写入数据库
            Image image = new Image();
            image.setPath(fileName);
            image.setType(type);
            image.setUid(userId);
            imageMapper.insert(image);
            //修改用户信息
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id",userId);
            updateWrapper.set(type,fileName);
            userMapper.update(null,updateWrapper);

            jsonObject.put("code", 200);
            jsonObject.put("msg", "上传成功");
            jsonObject.put("url", fileName);
        }finally {

        }
        return jsonObject;
    }
}
