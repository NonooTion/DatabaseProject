package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.damain.PracticeArea;
import com.gym.damain.User;
import com.gym.dao.PracticeAreaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/practiceArea")
public class PracticeAreaController {
    @Value("${PRACTICE_AREA_IMAGE_PATH}")
    String UPLOAD_DIR;
    @Value("${PRACTICE_AREA_IMAGE_SAVE_PATH}")
    String SAVE_PATH;
    @Autowired
    PracticeAreaDao practiceAreaDao;
    @PostMapping
    Result addPracticeArea(@RequestBody PracticeArea practiceArea)
    {
        //增加操作，将主键id设置成null
        practiceArea.setAreaId(null);
        //添加
        practiceAreaDao.insert(practiceArea);
        return new Result(Code.ADD_SUCCESS,null,"添加成功");
    }
    @GetMapping
    Result getPracticeArea(@RequestParam(required = false)String areaName)
    {
        QueryWrapper<PracticeArea> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(areaName!=null&&!areaName.isEmpty(),"area_name","%"+areaName+"%");
        List<PracticeArea> practiceAreas = practiceAreaDao.selectList(queryWrapper);
        return new Result(Code.SELECT_SUCCESS,practiceAreas,"查找成功");
    }
    @PostMapping("/upload")
    Result upload(@RequestParam MultipartFile file,@RequestParam String areaId)
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
            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(areaId+extension);
            //删除相同文件名的文件
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(UPLOAD_DIR), path -> {
                String fileName = path.getFileName().toString();
                int dotIndex = fileName.lastIndexOf('.');
                return dotIndex > 0 && fileName.substring(0, dotIndex).equals(areaId);
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
            PracticeArea practiceArea=new PracticeArea();
            practiceArea.setAreaId(Integer.valueOf(areaId));
            practiceArea.setImageUrl(SAVE_PATH+areaId+extension);
            practiceAreaDao.updateById(practiceArea);
        }
        return new Result(Code.UPDATE_SUCCESS, null, "上传成功");
    }

    @DeleteMapping
    Result deletePracticeArea(@RequestParam(required = true)String areaId)
    {
        if (areaId!=null) {
            practiceAreaDao.deleteById(areaId);
            //删除对应的图片
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(UPLOAD_DIR), path -> {
                String fileName = path.getFileName().toString();
                int dotIndex = fileName.lastIndexOf('.');
                return dotIndex > 0 && fileName.substring(0, dotIndex).equals(areaId);
            })) {
                for (Path entry : stream) {
                    Files.delete(entry); // 删除所有具有相同前缀的文件
                }
            } catch (IOException | DirectoryIteratorException e) {
                throw new RuntimeException("无法删除具有相同前缀的文件", e);
            }
            return new Result(Code.DELETE_SUCCESS,null,"删除成功");
        }
        return new Result(Code.DELETE_FAILURE,null,"删除失败");
    }

    @PutMapping
    Result updateCustomer(@RequestBody PracticeArea practiceArea)
    {
        if(practiceArea!=null)
        {
            practiceAreaDao.updateById(practiceArea);
            return new Result(Code.UPDATE_SUCCESS,null,"更新成功");
        }
        return new Result(Code.UPDATE_FAILURE,null,"更新失败");
    }
}
