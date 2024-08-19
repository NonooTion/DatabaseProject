package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "practice_areas")
@Data
public class PracticeArea {
    @TableId(value = "area_id",type = IdType.AUTO)
    Integer areaId;
    @TableField(value = "area_name")
    String areaName;
    String description;
    @TableField(value = "image_url")
    String imageUrl;
}
