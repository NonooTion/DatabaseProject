package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.*;
import com.gym.dao.*;
import com.gym.tool.TimeCheckTool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customerCourse")
public class CustomerCourseController {
    @Autowired
    CustomerCourseDao customerCourseDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GroupClassDao groupClassDao;
    @Autowired
    TimeCheckTool timeCheckTool;
    @Autowired
    MembershipDao membershipDao;
    @Autowired
    TransactionDao transactionDao;
    @Data
    public class CustomerCourseDTO{
        Integer customerCourseId;
        Integer classId;
        String className;
        Integer coachId;
        String coachName;
        LocalDate startDate;
        LocalDate endDate;
        LocalTime startTime;
        LocalTime endTime;

        Integer customerId;
        String customerName;
        CustomerCourseDTO(CustomerCourse customerCourse)
        {
            User user = userDao.selectById(customerCourse.getCustomerId());
            GroupClass groupClass = groupClassDao.selectById(customerCourse.getClassId());
            User coach= userDao.selectById(groupClass.getCoachId());

            this.customerId=user.getUserId();
            this.customerName=user.getName();
            this.classId=groupClass.getClassId();
            this.coachId=groupClass.getCoachId();
            this.customerCourseId=customerCourse.getCustomerCourseId();
            this.coachName=coach.getName();
            this.startDate=groupClass.getStartDate();
            this.endDate=groupClass.getEndDate();
            this.startTime=groupClass.getStartTime();
            this.endTime=groupClass.getEndTime();
            this.className=groupClass.getClassName();
        }
    }

    @GetMapping
    Result findCustomerCourse(@RequestParam(required = false) String customerId,
                              @RequestParam(required = false)String classId)
    {
        QueryWrapper<CustomerCourse> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(customerId!=null&&!customerId.isEmpty(),"customer_id",customerId);
        queryWrapper.eq(classId!=null&&!classId.isEmpty(),"class_id",classId);
        List<CustomerCourse> customerCourses = customerCourseDao.selectList(queryWrapper);
        List<CustomerCourseDTO> customerCourseDTOS=new ArrayList<>();
        for (CustomerCourse c:customerCourses)
        {
            customerCourseDTOS.add(new CustomerCourseDTO(c));
        }
        return new Result(Code.SELECT_SUCCESS,customerCourseDTOS,"查询成功");
    }
    @PutMapping
    Result updateCustomerCourse(@RequestBody CustomerCourse customerCourse)
    {
        String status = timeCheckTool.checkPrivateCoachTime(customerCourse);
        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.UPDATE_FAILURE,null,"教练私教时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.UPDATE_FAILURE,null,"客户私教时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.UPDATE_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }
        //检查顾客团课时间是否冲突
        status =timeCheckTool.checkGroupClassTime(customerCourse);
        if (status.equals(timeCheckTool.COACH_TIME_CONFLICT))
        {
            return new Result(Code.UPDATE_FAILURE,null,"教练团课时间冲突");
        }
        else if(status.equals(timeCheckTool.CUSTOMER_TIME_CONFLICT))
        {
            return new Result(Code.UPDATE_FAILURE,null,"客户团课时间冲突");
        }
        else  if(status.equals(timeCheckTool.TIME_ERROR))
        {
            return new Result(Code.UPDATE_FAILURE,null,"时间异常，开始时间晚于结束时间");
        }
        customerCourseDao.updateById(customerCourse);
        return new Result(Code.UPDATE_SUCCESS,null,"修改成功");
    }
    @PostMapping
    Result addCustomerCourse(@RequestBody CustomerCourse customerCourse)
    {
        //检查顾客私教时间是否冲突
        String status = timeCheckTool.checkPrivateCoachTime(customerCourse);
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
        //检查顾客团课时间是否冲突
        status =timeCheckTool.checkGroupClassTime(customerCourse);
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
        customerCourseDao.insert(customerCourse);
        return new Result(Code.ADD_SUCCESS,null,"添加成功");
    }

    @PostMapping("/payment")
    Result buyCustomerCourse(@RequestBody CustomerCourse customerCourse)
    {
        System.out.println(customerCourse);
        //检查顾客私教时间是否冲突
        String status = timeCheckTool.checkPrivateCoachTime(customerCourse);
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
        //检查顾客团课时间是否冲突
        status =timeCheckTool.checkGroupClassTime(customerCourse);
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
        QueryWrapper<Membership> qw=new QueryWrapper<>();
        qw.eq("user_id",customerCourse.getCustomerId());
        Membership membership = membershipDao.selectOne(qw);
        GroupClass groupClass = groupClassDao.selectById(customerCourse.getClassId());
        if (membership.getBalance().compareTo(groupClass.getPrice())<0)
        {
            return new Result(Code.ADD_FAILURE,null,"购买失败，余额不足");
        }
        membership.setBalance(membership.getBalance().subtract(groupClass.getPrice()));
        //交易信息保存
        Transaction transaction=new Transaction();
        transaction.setUserId(customerCourse.getCustomerId());
        transaction.setTransactionType("团课");
        transaction.setAmount(groupClass.getPrice());
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setDescription("购买团课");
        transactionDao.insert(transaction);
        membershipDao.updateById(membership);
        //购买团课
        customerCourseDao.insert(customerCourse);
        return new Result(Code.ADD_SUCCESS,null,"购买成功");
    }
    @DeleteMapping
    Result deleteCustomer(@RequestParam(required = true)Integer customerCourseId)
    {
        if (customerCourseId!=null) {
            customerCourseDao.deleteById(customerCourseId);
            return new Result(Code.DELETE_SUCCESS,null,"删除成功");
        }
        return new Result(Code.DELETE_FAILURE,null,"删除失败");
    }

}
