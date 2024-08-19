package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "coaches")
public class Coach {
    @TableId(value = "coach_id",type = IdType.AUTO)
    Integer coachId;
    @TableField(value="rate_per_hour")
    BigDecimal ratePerHour;

    String introduce;

    @TableField(value = "image_url")
    String imageUrl;
}
