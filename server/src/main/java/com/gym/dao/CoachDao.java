package com.gym.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.damain.Coach;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoachDao extends BaseMapper<Coach> {
}
