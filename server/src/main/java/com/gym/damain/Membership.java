package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 会员卡类
 */

@Data
@TableName(value = "memberships")
public class Membership {
    @TableId(value="membership_id" ,type = IdType.AUTO)
    Integer membershipId;
    @TableField(value = "user_id")
    Integer userId;
    @TableField("membership_num")
    String membershipNum;
    @TableField(value="membership_type")
    String membershipType;
    @TableField(value = "start_date")
    Date startDate;
    @TableField(value="end_date")
    Date endDate;
    String status;
    BigDecimal balance;
    Long remainder;
}
