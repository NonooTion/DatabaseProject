package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

//会员卡信息
@Data
@TableName(value = "membership_card")
public class MembershipCard {
    @TableId(value = "id",type = IdType.AUTO)
    Integer id;
    String type;
    BigDecimal price;
}
