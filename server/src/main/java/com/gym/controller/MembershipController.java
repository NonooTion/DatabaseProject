package com.gym.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gym.damain.Membership;
import com.gym.damain.Transaction;
import com.gym.damain.User;
import com.gym.dao.MembershipDao;
import com.gym.dao.TransactionDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    MembershipDao membershipDao;
    @Autowired
    TransactionDao transactionDao;
    /**
     * 添加会员信息
     * @param membership
     * @return
     */
    @PostMapping
    Result addMembership(@RequestBody Membership membership) {
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

    @GetMapping
    Result findMembership(@RequestParam(required = false) String membershipNum)
    {
        QueryWrapper<Membership> qw= new QueryWrapper<>();
        qw.eq(membershipNum!=null&&!membershipNum.isEmpty(),"membership_num",membershipNum);
        List<Membership> memberships = membershipDao.selectList(qw);
        System.out.println(memberships);
        Result result=new Result();
        result.setData(memberships);
        if(memberships.isEmpty()) {
            result.setCode(Code.SELECT_FAILURE);
            result.setMessage("无查询结果，请检查查询条件");
        }
        result.setCode(Code.SELECT_SUCCESS);
        result.setMessage("查询成功");
        return result;
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
    @Data
    public static class upgradeDto{
        Membership membership;
        String updateType;
        BigDecimal price;
    }
    @PutMapping("/upgrade")
    Result upgradeCard(@RequestBody upgradeDto upgradeDto)
    {

        Membership membership=upgradeDto.getMembership();
        String updateType= upgradeDto.getUpdateType();
        BigDecimal price= upgradeDto.getPrice();
        BigDecimal balance=membership.getBalance();
        Date startDate=null,endDate = null;
        if(balance.compareTo(price)<0)
        {
            return new Result(Code.UPDATE_FAILURE,null,"余额不足");
        }
        //扣费
        balance=balance.subtract(price);
        membership.setBalance(balance);
        Transaction transaction=new Transaction();
        transaction.setDescription("会员卡升级为"+updateType);
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setAmount(price);
        transaction.setUserId(membership.getUserId());
        transaction.setTransactionType("会员卡升级");
        transactionDao.insert(transaction);
        //处理次卡升级为月卡的情况
        if (updateType.equals("月卡"))
        {
            //获取现在的时间
            startDate= Date.valueOf(LocalDate.now());
            endDate= Date.valueOf(startDate.toLocalDate().plusMonths(1));
        } else if (updateType.equals("年卡")) {
            if (membership.getMembershipType().equals("月卡"))
            {
                //获取月卡剩余时间
                Long remainder=membership.getEndDate().getTime()-System.currentTimeMillis();
                //获取现在的时间
                startDate= Date.valueOf(LocalDate.now());
                endDate= Date.valueOf(startDate.toLocalDate().plusYears(1));
                //加上月卡的剩余时间
                endDate=new Date(endDate.getTime()+remainder);
            }
            else {
                startDate= Date.valueOf(LocalDate.now());
                endDate= Date.valueOf(startDate.toLocalDate().plusYears(1));
            }
        }
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setMembershipType(updateType);
        membershipDao.updateById(membership);
        return new Result(Code.UPDATE_SUCCESS,null,"您的会员卡成功升级为"+updateType);
    }
    //删除会员卡信息
    @DeleteMapping
    Result deleteMembership(@RequestParam(required = true)Integer membershipId)
    {
        if (membershipId!=null) {
            membershipDao.deleteById(membershipId);
            return new Result(Code.DELETE_SUCCESS,null,"删除成功");
        }
        return new Result(Code.DELETE_FAILURE,null,"删除失败");
    }

    //会员卡充值
    @PostMapping("/recharge")
    Result rechargeTransaction(@RequestBody Transaction transaction)
    {
        Integer userId=transaction.getUserId();
        if (userId==null) return new Result(Code.ADD_FAILURE,null,"充值失败，会员信息异常!");
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        //存储交易信息
        transactionDao.insert(transaction);
        //获取用户ID，修改用户会员卡余额
        QueryWrapper<Membership> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        Membership membership = membershipDao.selectOne(qw);
        BigDecimal balance=membership.getBalance();
        balance=balance.add(transaction.getAmount());
        membership.setBalance(balance);
        membershipDao.updateById(membership);
        return new Result(Code.ADD_SUCCESS,membership,"充值成功，感谢支持!");
    }
}
