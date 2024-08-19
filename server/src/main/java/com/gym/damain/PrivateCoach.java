package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@TableName("private_coaches")
@Data
public class PrivateCoach {
    @TableId(value = "private_coach_id",type = IdType.AUTO)
    Integer privateCoachId;
    @TableField(value = "customer_id")
    Integer customerId;
    @TableField(value = "coach_id")
    Integer coachId;
    LocalDate date;
    @TableField(value = "start_time")
    LocalTime startTime;
    @TableField(value = "end_time")
    LocalTime endTime;
}
