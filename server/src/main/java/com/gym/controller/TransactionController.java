package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.Membership;
import com.gym.damain.Transaction;
import com.gym.dao.MembershipDao;
import com.gym.dao.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    MembershipDao membershipDao;
    @Autowired
    TransactionDao transactionDao;

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
