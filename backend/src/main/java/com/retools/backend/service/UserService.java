package com.retools.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.retools.backend.entity.Code;
import com.retools.backend.entity.User;
import com.retools.backend.mapper.CodeMapper;
import com.retools.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CodeMapper codeMapper;

    public int register(User user,String code){
        List<User> userList = userMapper.selectList(null);
        for (User u : userList) {
            if (u.getEmail()!=null && u.getEmail().equals(user.getEmail())) {
                return 2;
            }else if(u.getPhone()!=null && u.getPhone().equals(user.getPhone())){
                return 3;
            }
        }
        if(code==null)
            return 4;
        List<Code> codeList = codeMapper.selectList(new QueryWrapper<>());
        for (Code c : codeList) {
            if (c.getCode().equals(code)) {
                codeMapper.deleteById(c);
                return userMapper.insert(user);
            }
        }
        return 4;
    }

}
