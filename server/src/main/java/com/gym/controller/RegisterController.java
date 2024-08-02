package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.User;
import com.gym.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class RegisterController {
    @Autowired
    UserDao userDao;

    @PostMapping("/register")
    Result userRegister(@RequestBody User user)
    {
        String userName=user.getUserName();
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq("user_name",userName);

        User theUser = userDao.selectOne(qw);
        if(theUser!=null) return new Result(Code.REGISTER_FAILURE,null,"用户名已存在");
        userDao.insert(user);
        return new Result(Code.REGISTER_SUCCESS,null,"注册成功");
    }

    /**
     * 重置密码
     * @param theUser
     * @return
     */
    @PutMapping("/reset")
    Result resetPassword(@RequestBody User theUser)
    {
        String userName=theUser.getUserName();
        String tel= theUser.getTel();
        String newPassword=theUser.getPassword();
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq("user_name",userName);
        User user = userDao.selectOne(qw);

        //查找是否存在用户名为userName的用户
        if(user==null) return new Result(Code.RESET_FAILURE,null,"用户名不存在!");

        //如果存在，判断手机号是否为tel
        else if(!user.getTel().equals(tel)) return new Result(Code.RESET_FAILURE,null,"用户名与手机号不匹配!");

        //手机号为tel，密码是否与原密码相同
        else if(user.getPassword().equals(newPassword))return new Result(Code.RESET_FAILURE,null,"密码与原密码相同");

        //设置新密码
        else{
            user.setPassword(newPassword);
            userDao.updateById(user);
        }

        return new Result(Code.RESET_SUCCESS,null,"重置密码成功！");
    }

}
