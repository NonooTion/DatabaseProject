package com.gym.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("customer_course")
@Data
public class CustomerCourse {
    @TableId(value = "customer_course_id",type = IdType.AUTO)
    Integer customerCourseId;
    @TableField(value = "customer_id")
    Integer customerId;
    @TableField(value = "class_id")
    Integer classId;
}
