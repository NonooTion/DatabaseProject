package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@TableName("group_classes")
public class GroupClass {
    @TableId(value = "class_id",type = IdType.AUTO)
    Integer classId;
    @TableField(value = "class_name")
    String className;
    @TableField(value = "coach_id")
    Integer coachId;
    String description;
    @TableField(value = "start_date")
    LocalDate startDate;
    @TableField(value = "end_date")
    LocalDate endDate;
    @TableField(value = "start_time")
    LocalTime startTime;
    @TableField(value = "end_time")
    LocalTime endTime;
    @TableField(value = "image_url")
    String imageUrl;
    BigDecimal price;
}
