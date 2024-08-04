package com.gym.damain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@TableName("transaction")
public class Transaction
{
    @TableId(value = "transaction_id",type = IdType.AUTO)
    Integer transactionId;
    @TableField("user_id")
    Integer userId;
    @TableField("transaction_type")
    String transactionType;
    BigDecimal amount;
    @TableField("transaction_date")
    Timestamp transactionDate;
    String description;
}
