package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.CustomerCourse;
import com.gym.damain.GroupClass;
import com.gym.damain.PrivateCoach;
import com.gym.dao.CustomerCourseDao;
import com.gym.dao.GroupClassDao;
import com.gym.dao.PrivateCoachDao;
import com.gym.dao.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    GroupClassDao groupClassDao;
    @Autowired
    PrivateCoachDao privateCoachDao;
    @Autowired
    CustomerCourseDao customerCourseDao;
    @Autowired
    UserDao userDao;
    @Data
    public class CustomerScheduleDto
    {
        LocalDate Date;
        LocalTime startTime;
        LocalTime endTime;
        String title;
        String details;
        CustomerScheduleDto(PrivateCoach privateCoach)
        {
            this.Date=privateCoach.getDate();
            this.startTime=privateCoach.getStartTime();
            this.endTime=privateCoach.getEndTime();
            this.title="私教预约";
            this.details="与"+userDao.selectById(privateCoach.getCoachId()).getName()+"教练预约健身";
        }
        CustomerScheduleDto(GroupClass groupClass)
        {
            this.Date=LocalDate.now();
            this.startTime=groupClass.getStartTime();
            this.endTime=groupClass.getEndTime();
            this.title="团课-"+groupClass.getClassName();
            this.details="请按时参加"+groupClass.getClassName()+"课程";
        }
    }
    @GetMapping("/customer")
    public Result getCustomerSchedule(@RequestParam(required = true) String userId)
    {
        List<CustomerScheduleDto> scheduleDtoList=new ArrayList<>();
        QueryWrapper<PrivateCoach> privateCoachQueryWrapper=new QueryWrapper<>();
        privateCoachQueryWrapper.eq("customer_id",userId);
        privateCoachQueryWrapper.eq("date",LocalDate.now());
        List<PrivateCoach> privateCoaches = privateCoachDao.selectList(privateCoachQueryWrapper);
        for (PrivateCoach p:privateCoaches)
        {
            scheduleDtoList.add(new CustomerScheduleDto(p));
        }
        QueryWrapper<CustomerCourse> customerCourseQueryWrapper=new QueryWrapper<>();
        customerCourseQueryWrapper.eq("customer_id",userId);
        List<CustomerCourse> customerCourses = customerCourseDao.selectList(customerCourseQueryWrapper);
        QueryWrapper<GroupClass> classQueryWrapper=new QueryWrapper<>();
        for (CustomerCourse c:customerCourses)
        {
            Integer classId = c.getClassId();
            classQueryWrapper.eq("class_id",classId);
            classQueryWrapper.le("start_date", LocalDate.now()) // 开始日期小于等于今天
                    .ge("end_date", LocalDate.now());  // 结束日期大于等于今天
            GroupClass groupClass = groupClassDao.selectOne(classQueryWrapper);
            if(groupClass!=null)
            {
                scheduleDtoList.add(new CustomerScheduleDto(groupClass));
            }
        }
        return new Result(Code.SELECT_SUCCESS,scheduleDtoList,"查找成功");
    }

    @Data
    public class CoachScheduleDto
    {
        LocalDate Date;
        LocalTime startTime;
        LocalTime endTime;
        String title;
        String details;
        CoachScheduleDto(PrivateCoach privateCoach)
        {
            this.Date=privateCoach.getDate();
            this.startTime=privateCoach.getStartTime();
            this.endTime=privateCoach.getEndTime();
            this.title="私教预约";
            this.details="与"+userDao.selectById(privateCoach.getCustomerId()).getName()+"学员预约健身";
        }
        CoachScheduleDto(GroupClass groupClass)
        {
            this.Date=LocalDate.now();
            this.startTime=groupClass.getStartTime();
            this.endTime=groupClass.getEndTime();
            this.title="团课-"+groupClass.getClassName();
            this.details="请按时参加"+groupClass.getClassName()+"课程";
        }
    }
    @GetMapping("/coach")
    public Result getCoachSchedule(@RequestParam(required = true) String userId)
    {
        List<CoachScheduleDto> scheduleDtoList=new ArrayList<>();
        QueryWrapper<PrivateCoach> privateCoachQueryWrapper=new QueryWrapper<>();
        privateCoachQueryWrapper.eq("coach_id",userId);
        privateCoachQueryWrapper.eq("date",LocalDate.now());
        List<PrivateCoach> privateCoaches = privateCoachDao.selectList(privateCoachQueryWrapper);
        for (PrivateCoach p:privateCoaches)
        {
            scheduleDtoList.add(new CoachScheduleDto(p));
        }
        QueryWrapper<GroupClass> groupClassQueryWrapper=new QueryWrapper<>();
            groupClassQueryWrapper.eq("coach_id",userId);
            groupClassQueryWrapper.le("start_date", LocalDate.now()) // 开始日期小于等于今天
                    .ge("end_date", LocalDate.now());  // 结束日期大于等于今天
        List<GroupClass> groupClasses = groupClassDao.selectList(groupClassQueryWrapper);
        for (GroupClass g:groupClasses)
        {
            scheduleDtoList.add(new CoachScheduleDto(g));
        }
        return new Result(Code.SELECT_SUCCESS,scheduleDtoList,"查找成功");
    }
}
