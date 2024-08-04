package com.gym.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.damain.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionDao extends BaseMapper<Transaction> {
}
