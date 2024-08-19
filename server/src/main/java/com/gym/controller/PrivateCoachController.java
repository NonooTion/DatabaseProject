package com.gym.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.Membership;
import com.gym.damain.PrivateCoach;
import com.gym.damain.Transaction;
import com.gym.damain.User;
import com.gym.dao.*;
import com.gym.tool.TimeCheckTool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/privateCoach")
public class PrivateCoachController {
    @Autowired
    TimeCheckTool timeCheckTool;
    @Autowired
    PrivateCoachDao privateCoachDao;
    @Autowired
    UserDao userDao;
    @Autowired
    CoachDao coachDao;
    @Autowired
    MembershipDao membershipDao;
    @Autowired
    TransactionDao transactionDao;

    @Data
    static class PrivateCoachDto{
        Integer privateCoachId;
        Integer customerId;
        String customerName;
        Integer coachId;
        String coachName;
        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;
        PrivateCoachDto(PrivateCoach privateCoach){
            this.date=privateCoach.getDate();
            this.privateCoachId=privateCoach.getPrivateCoachId();
            this.coachId=privateCoach.getCoachId();
            this.customerId=privateCoach.getCustomerId();
            this.startTime=privateCoach.getStartTime();
            this.endTime=privateCoach.getEndTime();
        }
    }
    List<PrivateCoachDto> transformData(List<PrivateCoach> privateCoaches)
    {
        List<PrivateCoachDto> data=new ArrayList<>();
        for(PrivateCoach privateCoach:privateCoaches)
        {
            PrivateCoachDto privateCoachDto= new PrivateCoachDto(privateCoach);
            privateCoachDto.setCoachName(userDao.selectById(privateCoach.getCoachId()).getName());
            privateCoachDto.setCustomerName(userDao.selectById(privateCoach.getCustomerId()).getName());
            data.add(privateCoachDto);
        }
        return data;
    }

    @PostMapping
    Result addPracticeArea(@RequestBody PrivateCoach privateCoach)
    {

        //检查时间是否冲突
        String status = timeCheckTool.checkPrivateCoachTime(privateCoach);

        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"教练私教时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"客户私教时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.ADD_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }

        status=timeCheckTool.checkGroupClassTime(privateCoach);
        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"教练团课时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"客户团课时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.ADD_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }

        privateCoachDao.insert(privateCoach);
        return new Result(Code.ADD_SUCCESS,null,"添加成功");
    }

    @PostMapping("/schedule")
    Result schedulePracticeArea(@RequestBody PrivateCoach privateCoach)
    {
        String status = timeCheckTool.checkPrivateCoachTime(privateCoach);

        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"教练私教时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"客户私教时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.ADD_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }

        status=timeCheckTool.checkGroupClassTime(privateCoach);
        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"教练团课时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"客户团课时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.ADD_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }

        //收取用户费用
        QueryWrapper<Membership> qw=new QueryWrapper<>();
        qw.eq("user_id",privateCoach.getCustomerId());
        Membership user = membershipDao.selectOne(qw);
        long hours = privateCoach.getEndTime().getHour()- privateCoach.getStartTime().getHour();

        double ratePerHour= coachDao.selectById(privateCoach.getCoachId()).getRatePerHour().doubleValue();

        BigDecimal amount=new BigDecimal(ratePerHour*hours);

        if (user.getBalance().compareTo(amount)<0)
        {
            return new Result(Code.ADD_FAILURE,null,"余额不足，请充值");
        }
        user.setBalance(user.getBalance().subtract(amount));
        membershipDao.updateById(user);
        //存储交易信息
        Transaction transaction=new Transaction();
        transaction.setAmount(amount);
        transaction.setUserId(user.getUserId());
        transaction.setDescription("预约私教");
        transaction.setTransactionType("私教");
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transactionDao.insert(transaction);
        privateCoachDao.insert(privateCoach);
        return new Result(Code.ADD_SUCCESS,null,"预约成功");
    }
    @GetMapping
    Result findPrivateCoaches(@RequestParam(required = false) String coachName,
                              @RequestParam(required = false) String customerName)
    {
        List<User> customers=null;
        List<User> coaches=null;
        List<PrivateCoach> privateCoaches=new ArrayList<>();
        if(coachName!=null&&!coachName.isEmpty())
        {
            QueryWrapper<User> queryWrapper=new QueryWrapper<>();
            queryWrapper.like("name",coachName);
            coaches= userDao.selectList(queryWrapper);
        }
        if(customerName!=null&&!customerName.isEmpty())
        {
            QueryWrapper<User> queryWrapper=new QueryWrapper<>();
            queryWrapper.like("name",customerName);
            customers= userDao.selectList(queryWrapper);
        }
        QueryWrapper<PrivateCoach> queryWrapper =new QueryWrapper<>();
        if(coaches!=null&&customers!=null)
        {
            for(User coach: coaches)
            {
               Integer coachId= coach.getUserId();
               for(User customer: customers)
               {
                   Integer customerId= customer.getUserId();
                   queryWrapper.eq("coach_id",coachId);
                   queryWrapper.eq("customer_id",customerId);
                   privateCoaches.addAll(privateCoachDao.selectList(queryWrapper));
               }
            }
        }
        else if(coaches==null&&customers!=null){
                for(User customer: customers)
                {
                    Integer customerId= customer.getUserId();
                    queryWrapper.eq("customer_id",customerId);
                    privateCoaches.addAll(privateCoachDao.selectList(queryWrapper));
                }
        }
        else if(coaches != null){
                for(User customer: coaches)
                {
                    Integer coachId= customer.getUserId();
                    queryWrapper.eq("coach_id",coachId);
                    privateCoaches.addAll(privateCoachDao.selectList(queryWrapper));
                }
        }
        else{
            privateCoaches=privateCoachDao.selectList(null);
        }
        List<PrivateCoachDto> data = transformData(privateCoaches);
        return new Result(Code.SELECT_SUCCESS,data,"查找成功");
    }

    @DeleteMapping
    Result delete(@RequestParam(required = true) String privateCoachId)
    {

        if (privateCoachId!=null) {
            privateCoachDao.deleteById(privateCoachId);
            return new Result(Code.DELETE_SUCCESS,null,"删除成功");
        }
        return new Result(Code.DELETE_FAILURE,null,"删除失败");
    }
    @PutMapping
    Result update(@RequestBody PrivateCoach privateCoach)
    {
        //检查时间是否冲突
        String status = timeCheckTool.checkPrivateCoachTime(privateCoach);

        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"教练私教时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"客户私教时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.ADD_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }

        status=timeCheckTool.checkGroupClassTime(privateCoach);
        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"教练团课时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.ADD_FAILURE,null,"客户团课时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.ADD_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }

        privateCoachDao.updateById(privateCoach);
        return new Result(Code.UPDATE_SUCCESS,null,"修改成功");
    }
}
