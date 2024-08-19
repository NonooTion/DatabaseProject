package com.gym.controller;

import com.gym.dao.MembershipCardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/membershipCard")
public class MembershipCardController {
    @Autowired
    MembershipCardDao membershipCardDao;
    @GetMapping
    Result getAll()
    {
        return new Result(Code.SELECT_SUCCESS,membershipCardDao.selectList(null),"查找成功");
    }
}
