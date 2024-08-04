package com.gym.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gym.damain.Membership;
import com.gym.dao.MembershipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    MembershipDao membershipDao;

    /**
     * 添加会员信息
     * @param membership
     * @return
     */
    @PostMapping
    Result addMembership(@RequestBody Membership membership) {
             System.out.println(membership);
            membershipDao.insert(membership);
            Integer id= membership.getMembershipId();
            String prefix = "GYM";
            String formattedId = String.format("%08d", membership.getMembershipId());
            membership.setMembershipNum(prefix + formattedId);
            membershipDao.updateById(membership);
            return new Result(Code.ADD_SUCCESS,null, "添加成功");
    }

    /**
     * 通过用户Id查找会员信息
     * @param userId
     * @return
     */
    @GetMapping("/findByUserId")
    Result getMembership(@RequestParam(required = false) Integer userId)
    {
        QueryWrapper<Membership> qw=new QueryWrapper<>();
        if (userId!=null) {
            qw.eq("user_id", userId);
            Membership membership = membershipDao.selectOne(qw);
            if(membership!=null){
            return new Result(Code.SELECT_SUCCESS, membership, "查询成功");
            }
        }
        return new Result(Code.SELECT_FAILURE,null,"该用户无会员卡");
    }

    /**
     * 过期会员卡处理 设置为次卡
     * @param membership
     * @return
     */
    @PutMapping("/expire")
    Result expire(@RequestBody Membership membership){
        UpdateWrapper<Membership> uw=new UpdateWrapper<>();
        uw.eq("user_id",membership.getUserId()).set("membership_type","次卡")
                .set("start_date",null).set("end_date",null).set("remainder",null);
        membershipDao.update(uw);
        return new Result(Code.UPDATE_SUCCESS,null,"过期会员卡处理成功");
    }
    /**
     * 暂停会员卡
     * @param membership
     * @return
     */
    @PutMapping("/pause")
    Result pauseCard(@RequestBody Membership membership)
    {
        String type=membership.getMembershipType();
        if(!type.equals("次卡")){
            Long remainder=membership.getEndDate().getTime()-System.currentTimeMillis();
            membership.setRemainder(remainder);
        }
        membership.setStatus("暂停");
        membershipDao.updateById(membership);
        return new Result(Code.UPDATE_SUCCESS,null,"更新成功");
    }
    @PutMapping("/activation")
    Result activationCard(@RequestBody Membership membership)
    {
        String type=membership.getMembershipType();
        if(!type.equals("次卡")){
            Long remainder=membership.getRemainder();
            long current=System.currentTimeMillis();
            membership.setStartDate(new Date(current));
            membership.setEndDate(new Date(current+remainder));
            membership.setRemainder(0L);
        }
        membership.setStatus("激活");
        membershipDao.updateById(membership);
        return new Result(Code.UPDATE_SUCCESS,null,"更新成功");
    }
}
