package com.gym.tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.CustomerCourse;
import com.gym.damain.GroupClass;
import com.gym.damain.PrivateCoach;
import com.gym.dao.CustomerCourseDao;
import com.gym.dao.GroupClassDao;
import com.gym.dao.PrivateCoachDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TimeCheckTool {
    public final String TIME_ERROR="10";
    public final  String COACH_TIME_CONFLICT="00";
    public final  String CUSTOMER_TIME_CONFLICT="01";
    public final  String TIME_OK="11";
    @Autowired
    PrivateCoachDao privateCoachDao;
    @Autowired
    GroupClassDao groupClassDao;
    @Autowired
    CustomerCourseDao customerCourseDao;
    public String checkPrivateCoachTime(PrivateCoach privateCoach)
    {
        Integer privateCoachId=privateCoach.getPrivateCoachId();
        Integer coachId=privateCoach.getCoachId();
        Integer customerId=privateCoach.getCustomerId();
        LocalDate date=privateCoach.getDate();
        LocalTime startTime=privateCoach.getStartTime();
        LocalTime endTime = privateCoach.getEndTime();
        if(startTime.isAfter(endTime))
        {
            return TIME_ERROR;
        }
        if(customerId != null)
        {
            QueryWrapper<PrivateCoach> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("coach_id",coachId);
            List<PrivateCoach> privateCoaches = privateCoachDao.selectList(queryWrapper);
            //教练时间是否冲突
            for(PrivateCoach p: privateCoaches)
            {
                if(!date.isEqual(p.getDate()))
                {
                    continue;
                }
                if(startTime.isAfter(p.getEndTime())||endTime.isBefore(p.getStartTime()))
                {
                    continue;
                }
                else if(Objects.equals(p.getPrivateCoachId(), privateCoach.getPrivateCoachId()))
                {
                    continue;
                }
                else if(startTime.equals(p.getEndTime())||endTime.equals(p.getStartTime()))
                {
                    continue;
                }
                return COACH_TIME_CONFLICT;
            }
            queryWrapper.clear();
            queryWrapper.eq("customer_id",customerId);
            privateCoaches = privateCoachDao.selectList(queryWrapper);
            //客户时间是否冲突
            for(PrivateCoach p: privateCoaches)
            {
                if(!date.isEqual(p.getDate()))
                {
                    continue;
                }
                if(startTime.isAfter(p.getEndTime())||endTime.isBefore(p.getStartTime()))
                {
                    continue;
                }
                else if(Objects.equals(p.getPrivateCoachId(), privateCoach.getPrivateCoachId()))
                {
                    continue;
                }
                else if(startTime.equals(p.getEndTime())||endTime.equals(p.getStartTime()))
                {
                    continue;
                }
                return CUSTOMER_TIME_CONFLICT;
            }
        }
        return TIME_OK;
    }

    public String checkGroupClassTime(PrivateCoach privateCoach)
    {
        Integer coachId = privateCoach.getCoachId();
        Integer customerId = privateCoach.getCustomerId();
        LocalDate date = privateCoach.getDate();
        LocalTime startTime = privateCoach.getStartTime();
        LocalTime endTime = privateCoach.getEndTime();

        if(startTime.isAfter(endTime))
        {
            return TIME_ERROR;
        }
        QueryWrapper<GroupClass> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("coach_id",coachId);
        List<GroupClass> groupClasses = groupClassDao.selectList(queryWrapper);
        //教练团课时间是否冲突
        for(GroupClass g: groupClasses) {
            if (g.getEndDate().isBefore(date)||g.getStartDate().isAfter(date))
            {
                continue;
            }
            if(g.getEndTime().isBefore(startTime)||g.getStartTime().isAfter(endTime))
            {
                continue;
            }
            else if(startTime.equals(g.getEndTime())||endTime.equals(g.getStartTime()))
            {
                continue;
            }
            return COACH_TIME_CONFLICT;
        }

        QueryWrapper<CustomerCourse> qw=new QueryWrapper<>();
        qw.eq("customer_id",customerId);
        List<CustomerCourse> customerCourses = customerCourseDao.selectList(qw);
        groupClasses=new ArrayList<>();
        for(CustomerCourse c:customerCourses)
        {
            Integer classId = c.getClassId();
            groupClasses.add(groupClassDao.selectById(classId));
        }

        //客户团课时间是否冲突
        for(GroupClass g: groupClasses)
        {
            if (g.getEndDate().isBefore(date)||g.getStartDate().isAfter(date))
            {
                continue;
            }
            if(g.getEndTime().isBefore(startTime)||g.getStartTime().isAfter(endTime))
            {
                continue;
            }
            else if(startTime.equals(g.getEndTime())||endTime.equals(g.getStartTime()))
            {
                continue;
            }
            return CUSTOMER_TIME_CONFLICT;
        }
        return TIME_OK;
    }
    public String checkPrivateCoachTime(GroupClass groupClass)
    {
        Integer coachId=groupClass.getCoachId();
        LocalDate startDate = groupClass.getStartDate();
        LocalDate endDate = groupClass.getEndDate();
        LocalTime startTime = groupClass.getStartTime();
        LocalTime endTime = groupClass.getEndTime();


        if(startDate.isAfter(endDate)||(startDate.isBefore(endDate)&&startTime.isAfter(endTime)))
        {
            return TIME_ERROR;
        }
        else{
            QueryWrapper<PrivateCoach>queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("coach_id",coachId);
            List<PrivateCoach> privateCoaches = privateCoachDao.selectList(queryWrapper);
            //检查团课是否与教练私教的时间冲突
            for(PrivateCoach p : privateCoaches)
            {
                if (p.getDate().isAfter(endDate)||p.getDate().isBefore(startDate))
                {
                    continue;
                } else if (p.getStartTime().isAfter(endTime)||p.getEndTime().isBefore(startTime)) {
                    continue;
                }
                else if(startTime.equals(p.getEndTime())||endTime.equals(p.getStartTime()))
                {
                    continue;
                }
                return COACH_TIME_CONFLICT;
            }
        }
        return TIME_OK;
    }

    public String checkGroupClassTime(GroupClass groupClass)
    {
        Integer groupClassId=groupClass.getClassId();
        Integer coachId=groupClass.getCoachId();
        LocalDate startDate = groupClass.getStartDate();
        LocalDate endDate = groupClass.getEndDate();
        LocalTime startTime = groupClass.getStartTime();
        LocalTime endTime = groupClass.getEndTime();

        if(startDate.isAfter(endDate)||(startDate.isBefore(endDate)&&startTime.isAfter(endTime)))
        {
            return TIME_ERROR;
        }
            QueryWrapper<GroupClass> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("coach_id",coachId);
            List<GroupClass> groupClasses = groupClassDao.selectList(queryWrapper);
            //教练时间是否冲突
            for(GroupClass g: groupClasses) {
                if(g.getClassId().equals(groupClassId))
                {
                    continue;
                }
                if (g.getEndDate().isBefore(startDate)||g.getStartDate().isAfter(endDate))
                {
                    continue;
                }
                if(g.getEndTime().isBefore(startTime)||g.getStartTime().isAfter(endTime))
                {
                    continue;
                }
                else if(startTime.equals(g.getEndTime())||endTime.equals(g.getStartTime()))
                {
                    continue;
                }
                return COACH_TIME_CONFLICT;
            }
        return TIME_OK;
    }

    public String checkPrivateCoachTime(CustomerCourse customerCourse)
    {
        Integer customerId = customerCourse.getCustomerId();
        Integer customerCourseId = customerCourse.getCustomerCourseId();
        Integer classId= customerCourse.getClassId();

        GroupClass groupClass = groupClassDao.selectById(classId);
        LocalDate startDate = groupClass.getStartDate();
        LocalDate endDate = groupClass.getEndDate();
        LocalTime startTime = groupClass.getStartTime();
        LocalTime endTime = groupClass.getEndTime();

        QueryWrapper<PrivateCoach> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        List<PrivateCoach> privateCoaches = privateCoachDao.selectList(queryWrapper);
        //客户私教时间是否冲突
        for(PrivateCoach p: privateCoaches)
        {
            if(startDate.isAfter(p.getDate())||endDate.isBefore(p.getDate()))
            {
                continue;
            }
            if(startTime.isAfter(p.getEndTime())||endTime.isBefore(p.getStartTime()))
            {
                continue;
            }
            else if(startTime.equals(p.getEndTime())||endTime.equals(p.getStartTime()))
            {
                continue;
            }
            return CUSTOMER_TIME_CONFLICT;
        }
        return TIME_OK;
    }
    public String checkGroupClassTime(CustomerCourse customerCourse)
    {
        Integer customerId = customerCourse.getCustomerId();
        Integer classId = customerCourse.getClassId();
        Integer customerCourseId=customerCourse.getCustomerCourseId();
        GroupClass groupClass = groupClassDao.selectById(classId);
        LocalDate startDate = groupClass.getStartDate();
        LocalDate endDate = groupClass.getEndDate();
        LocalTime startTime = groupClass.getStartTime();
        LocalTime endTime = groupClass.getEndTime();

        QueryWrapper<CustomerCourse> qw=new QueryWrapper<>();
        qw.eq("customer_id",customerId);
        List<CustomerCourse> customerCourses = customerCourseDao.selectList(qw);
        List<GroupClass> groupClasses=new ArrayList<>();
        for(CustomerCourse c:customerCourses)
        {
            if (!Objects.equals(c.getCustomerCourseId(), customerCourseId))
            {
                groupClasses.add(groupClassDao.selectById(c.getClassId()));
            }
        }

        //客户团课时间是否冲突
        for(GroupClass g: groupClasses)
        {
            if (g.getEndDate().isBefore(startDate)||g.getStartDate().isAfter(endDate))
            {
                continue;
            }
            if(g.getEndTime().isBefore(startTime)||g.getStartTime().isAfter(endTime))
            {
                continue;
            }
            else if(startTime.equals(g.getEndTime())||endTime.equals(g.getStartTime()))
            {
                continue;
            }
            return CUSTOMER_TIME_CONFLICT;
        }
        return TIME_OK;
    }


}
