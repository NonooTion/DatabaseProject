package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.User;
import com.gym.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/login")
public class LoggingController {
    @Autowired
    UserDao userDao;

    /**
     * 用户登录功能
     * @param theUser
     * @return
     */
    @PostMapping
    Result login(@RequestBody User theUser) {
        //从前端获取登录账号的信息
        String userName=theUser.getUserName();
        String password=theUser.getPassword();

        //查询是否有这个账号
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", userName);
        User user = userDao.selectOne(queryWrapper);
        Result result = new Result();

        //如果账户存在，检查密码是否匹配
        if (user!=null&&user.getPassword().equals(password)) {
            result.setCode(Code.LOGIN_SUCCESS);
            result.setData(user);
            result.setMessage("登录成功");
        } else {
            result.setCode(Code.LOGIN_FAILURE);
            result.setMessage("登录失败,请确认您的账号和密码");
        }
        return result;
    }
}
