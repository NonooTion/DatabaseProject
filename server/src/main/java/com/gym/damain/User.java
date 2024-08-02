package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户类
 */
@Data
@TableName(value = "users")
public class User {
    @TableField(value = "user_id")
    @TableId(type = IdType.AUTO)
    Integer userId;
    String name;
    String gender;
    String tel;
    @TableField(value = "user_type")
    String userType;
    String email;
    @TableField(value = "user_name")
    String userName;
    String password;
}
