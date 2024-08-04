package com.gym.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.damain.Membership;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MembershipDao extends BaseMapper<Membership> {

}
