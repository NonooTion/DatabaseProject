package com.gym.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.Coach;
import com.gym.damain.GroupClass;
import com.gym.damain.PrivateCoach;
import com.gym.damain.User;
import com.gym.dao.GroupClassDao;
import com.gym.dao.UserDao;
import com.gym.tool.TimeCheckTool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/groupClass")
public class GroupClassController {
    @Autowired
    GroupClassDao groupClassDao;
    @Autowired
    UserDao userDao;
    @Autowired
    TimeCheckTool timeCheckTool;
    @Value("${GROUP_CLASS_IMAGE_PATH}")
    String UPLOAD_DIR;
    @Value("${GROUP_CLASS_IMAGE_SAVE_PATH}")
    String SAVE_PATH;
    @Data
    public  class GroupClassDTO{
        Integer classId;
        String className;
        Integer coachId;
        String coachName;
        String description;
        LocalDate startDate;
        LocalDate endDate;
        LocalTime startTime;
        LocalTime endTime;
        BigDecimal price;
        String imageUrl;
        GroupClassDTO(GroupClass groupClass)
        {
            this.classId=groupClass.getClassId();
            this.className=groupClass.getClassName();
            this.coachId=groupClass.getCoachId();
            this.endTime=groupClass.getEndTime();
            this.startTime=groupClass.getStartTime();
            this.startDate=groupClass.getStartDate();
            this.endDate=groupClass.getEndDate();
            this.imageUrl=groupClass.getImageUrl();
            this.description=groupClass.getDescription();
            this.price=groupClass.getPrice();
            User coach = userDao.selectById(this.coachId);
            this.coachName=coach.getName();
        }
    }
    @GetMapping
    Result selectGroupClass(@RequestParam(required = false) String className,
                            @RequestParam(required = false) String coachId,
                            @RequestParam(required = false) String date)
    {
        QueryWrapper<GroupClass> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(className!=null&&!className.isEmpty(),"class_name", "%"+className+"%");
        queryWrapper.eq(coachId!=null&&!coachId.isEmpty(),"coach_id",coachId);
        queryWrapper.ge(date!=null,"end_date",date);
        List<GroupClass> groupClasses = groupClassDao.selectList(queryWrapper);
        List<GroupClassDTO> groupClassDTOS=new ArrayList<>();
        for(GroupClass g:groupClasses)
        {
            groupClassDTOS.add(new GroupClassDTO(g));
        }
        return new Result(Code.SELECT_SUCCESS,groupClassDTOS,"查询成功");
    }
    @PutMapping
    Result update(@RequestBody GroupClass groupClass)
    {
        //检查时间是否冲突
        String status = timeCheckTool.checkPrivateCoachTime(groupClass);
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

        status=timeCheckTool.checkGroupClassTime(groupClass);
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
        groupClassDao.updateById(groupClass);
        return new Result(Code.UPDATE_SUCCESS,null,"修改成功");
    }
    @PostMapping
    Result addGroupClass(@RequestBody GroupClass groupClass)
    {

        //检查私教时间是否冲突
        String status = timeCheckTool.checkPrivateCoachTime(groupClass);
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
        //检查团课时间是否冲突
        status = timeCheckTool.checkGroupClassTime(groupClass);
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
        groupClassDao.insert(groupClass);
        return new Result(Code.ADD_SUCCESS,null,"添加成功");
    }
    @DeleteMapping
    Result delete(@RequestParam(required = true) String classId)
    {
        if (classId!=null) {
            groupClassDao.deleteById(classId);
            return new Result(Code.DELETE_SUCCESS,null,"删除成功");
        }
        return new Result(Code.DELETE_FAILURE,null,"删除失败");
    }

    @PostMapping("/upload")
    Result upload(@RequestParam MultipartFile file, @RequestParam String classId)
    {
        if(!file.isEmpty()) {
            // 获取文件的原始名称（包括扩展名）
            String originalFilename = file.getOriginalFilename();

            // 从原始文件名中提取扩展名
            String extension = "";
            int lastIndexOfDot = originalFilename.lastIndexOf('.');
            if (lastIndexOfDot > 0) {
                extension = originalFilename.substring(lastIndexOfDot);
            }
            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(classId+extension);
            //删除相同文件名的文件
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(UPLOAD_DIR), path -> {
                String fileName = path.getFileName().toString();
                int dotIndex = fileName.lastIndexOf('.');
                return dotIndex > 0 && fileName.substring(0, dotIndex).equals(classId);
            })) {
                for (Path entry : stream) {
                    Files.delete(entry); // 删除所有具有相同前缀的文件
                }
            } catch (IOException | DirectoryIteratorException e) {
                throw new RuntimeException("无法删除具有相同前缀的文件", e);
            }
            // 保存文件
            try {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //设置练习区域图片路径
            GroupClass groupClass=new GroupClass();
            groupClass.setClassId(Integer.valueOf(classId));
            groupClass.setImageUrl(SAVE_PATH+classId+extension);
            groupClassDao.updateById(groupClass);
        }
        return new Result(Code.UPDATE_SUCCESS, null, "上传成功");
    }
}
