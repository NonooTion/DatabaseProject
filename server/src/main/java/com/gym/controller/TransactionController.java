package com.gym.controller;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.Membership;
import com.gym.damain.PracticeArea;
import com.gym.damain.Transaction;
import com.gym.damain.User;
import com.gym.dao.MembershipDao;
import com.gym.dao.TransactionDao;
import com.gym.dao.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {
    @Autowired
    MembershipDao membershipDao;
    @Autowired
    TransactionDao transactionDao;
    @Autowired
    UserDao userDao;
    @Data
    public class TransactionDTO{
        Integer transactionId;
        Integer userId;
        String transactionType;
        BigDecimal amount;
        Timestamp transactionDate;
        String description;
        String userName;
        TransactionDTO(Transaction transaction)
        {
            this.transactionId=transaction.getTransactionId();
            this.userId=transaction.getUserId();
            this.transactionType=transaction.getTransactionType();
            this.amount=transaction.getAmount();
            this.transactionDate=transaction.getTransactionDate();
            this.description=transaction.getDescription();
            this.userName=userDao.selectById(this.userId).getName();
        }
    }
    @PostMapping
    Result addTransaction(@RequestBody Transaction transaction)
    {
        //增加操作，将主键id设置成null
        transaction.setTransactionId(null);
        //添加
        transactionDao.insert(transaction);
        return new Result(Code.ADD_SUCCESS,null,"添加成功");
    }
    @GetMapping
    Result getTransactions(@RequestParam(required = false)String userId)
    {
        QueryWrapper<Transaction> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(userId!=null&&!userId.isEmpty(),"user_id",userId);
        List<Transaction> transactions = transactionDao.selectList(queryWrapper);
        List<TransactionDTO> transactionDTOS=new ArrayList<>();
        for(Transaction t:transactions)
        {
            transactionDTOS.add(new TransactionDTO(t));
        }
        return new Result(Code.SELECT_SUCCESS,transactionDTOS,"查找成功");
    }

    @Data
    public class AnalyzeDTO{
        BigDecimal data;
        String type;
        AnalyzeDTO(Transaction transaction)
        {
            this.data=transaction.getAmount();
            this.type=transaction.getTransactionType();
        }
    }
    @GetMapping("/getByDate")
    Result getTransactionsByDate(@RequestParam(required = false)String startDate,
                                 @RequestParam(required = false)String endDate)
    {

        QueryWrapper<Transaction> queryWrapper=new QueryWrapper<>();
        queryWrapper.ge(startDate!=null&&!startDate.isEmpty(),"DATE(transaction_date)",startDate);
        queryWrapper.le(endDate!=null&&!endDate.isEmpty(),"DATE(transaction_date)",endDate);
        queryWrapper.groupBy("transaction_type");
        queryWrapper.select("sum(amount) as amount,transaction_type");
        List<Transaction> transactions = transactionDao.selectList(queryWrapper);
        List<AnalyzeDTO> analyzeDTOS = new ArrayList<>();
        for (Transaction transaction:transactions)
        {
            analyzeDTOS.add(new AnalyzeDTO(transaction));
        }
        return new Result(Code.SELECT_SUCCESS,analyzeDTOS,"查找成功");
    }
    @DeleteMapping
    Result deleteTransaction(@RequestParam(required = true)String transactionId)
    {
        if (transactionId!=null) {
            transactionDao.deleteById(transactionId);
            return new Result(Code.DELETE_SUCCESS,null,"删除成功");
        }
        return new Result(Code.DELETE_FAILURE,null,"删除失败");
    }

    @PutMapping
    Result updateCustomer(@RequestBody Transaction transaction)
    {
        if(transaction!=null)
        {
            transactionDao.updateById(transaction);
            return new Result(Code.UPDATE_SUCCESS,null,"更新成功");
        }
        return new Result(Code.UPDATE_FAILURE,null,"更新失败");
    }
}
