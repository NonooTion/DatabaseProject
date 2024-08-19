package com.gym.controller;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.Coach;
import com.gym.damain.PracticeArea;
import com.gym.damain.User;
import com.gym.dao.CoachDao;
import com.gym.dao.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coach")
public class CoachController {
    @Autowired
    UserDao userDao;
    @Autowired
    CoachDao coachDao;
    @Value("${COACH_IMAGE_PATH}")
    String UPLOAD_DIR;
    @Value("${COACH_IMAGE_SAVE_PATH}")
    String SAVE_PATH;

    @Data
    public  class CoachDTO{
        Integer coachId;
        BigDecimal ratePerHour;
        String introduce;
        String imageUrl;
        String name;
        CoachDTO(Coach coach)
        {
            this.coachId=coach.getCoachId();
            this.ratePerHour=coach.getRatePerHour();
            this.introduce=coach.getIntroduce();
            this.imageUrl= coach.getImageUrl();
            this.name=userDao.selectById(this.coachId).getName();
        }
    }
    @GetMapping
    Result getAllCoaches()
    {
        List<Coach> coaches = coachDao.selectList(null);
        List<CoachDTO> coachDTOS=new ArrayList<>();
        for (Coach c:coaches)
        {
            coachDTOS.add(new CoachDTO(c));
        }
        return new Result(Code.SELECT_SUCCESS,coachDTOS,"查找成功");
    }
    @GetMapping("/findById")
    Result getCoachInfoById(@RequestParam Integer userId)
    {
        QueryWrapper<Coach> queryWrapper=new QueryWrapper<>();
        if(userId!=null)
        {
            queryWrapper.eq("coach_id",userId);
        }
        Coach coach = coachDao.selectOne(queryWrapper);
        Result result=new Result();
        result.setCode(Code.SELECT_SUCCESS);
        result.setData(coach);
        result.setMessage("查询成功");
        return result;
    }
    @PutMapping
    Result updateCoachInfo(@RequestBody Coach coach)
    {
        if(coach!=null&&coach.getCoachId()>0)
        {
        coachDao.updateById(coach);
            return new Result(Code.UPDATE_SUCCESS,coach,"更新成功");
        }
        return new Result(Code.UPDATE_FAILURE,null,"更新失败");
    }

    @PostMapping("/upload")
    Result upload(@RequestParam MultipartFile file, @RequestParam String coachId)
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
            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(coachId+extension);
            //删除相同文件名的文件
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(UPLOAD_DIR), path -> {
                String fileName = path.getFileName().toString();
                int dotIndex = fileName.lastIndexOf('.');
                return dotIndex > 0 && fileName.substring(0, dotIndex).equals(coachId);
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
            Coach coach=new Coach();
            coach.setCoachId(Integer.valueOf(coachId));
            coach.setImageUrl(SAVE_PATH+coachId+extension);
            coachDao.updateById(coach);
        }
        return new Result(Code.UPDATE_SUCCESS, null, "上传成功");
    }
}
