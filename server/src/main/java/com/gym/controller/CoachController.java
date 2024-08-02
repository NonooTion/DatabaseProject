package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.User;
import com.gym.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach")
public class CoachController {
    @Autowired
    UserDao userDao;
    @GetMapping
    Result findCustomers(@RequestParam(required = false) String userType,
                         @RequestParam(required = false) String tel,
                         @RequestParam(required = false) String name)
    {
        QueryWrapper<User> qw= new QueryWrapper<>();
        qw.eq(!userType.isEmpty(),"user_type",userType);
        qw.eq(!tel.isEmpty(),"tel",tel);
        qw.eq(!name.isEmpty(),"name",name);
        List<User> users = userDao.selectList(qw);
        Result result=new Result();
        result.setData(users);
        if(users.isEmpty()) {
            result.setCode(Code.SELECT_FAILURE);
            result.setMessage("无查询结果，请检查查询条件");
        }
        result.setCode(Code.SELECT_SUCCESS);
        result.setMessage("查询成功");
        return result;
    }

    @PostMapping
    Result addCustomer(@RequestBody User user)
    {
        System.out.println(user);
        //增加操作，将主键id设置成null
        user.setUserId(null);
        //查询用户名是否已经存在
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq("user_name",user.getUserName());
        User user1 = userDao.selectOne(qw);
        if (user1!=null) return new Result(Code.ADD_FAILURE,null,"用户名已存在");
        //添加新用户
        userDao.insert(user);
        return new Result(Code.ADD_SUCCESS,null,"添加成功");
    }

    //删除教练
    @DeleteMapping
    Result deleteCustomer(@RequestParam(required = true)Integer userId)
    {
        if (userId!=null) {
            userDao.deleteById(userId);
            return new Result(Code.DELETE_SUCCESS,null,"删除成功");
        }
        return new Result(Code.DELETE_FAILURE,null,"删除失败");
    }

    //修改教练信息
    @PutMapping
    Result updateCustomer(@RequestBody User user)
    {
        if(user!=null)
        {
            userDao.updateById(user);
            return new Result(Code.UPDATE_SUCCESS,null,"更新成功");
        }
        return new Result(Code.UPDATE_FAILURE,null,"更新失败");
    }
}
