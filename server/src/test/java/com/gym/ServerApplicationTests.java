package com.gym;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.controller.MembershipController;
import com.gym.controller.ScheduleController;
import com.gym.damain.GroupClass;
import com.gym.damain.Membership;
import com.gym.damain.Transaction;
import com.gym.damain.User;
import com.gym.dao.GroupClassDao;
import com.gym.dao.MembershipDao;
import com.gym.dao.TransactionDao;
import com.gym.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

@SpringBootTest
class ServerApplicationTests {

    @Autowired
    UserDao userDao;

    @Autowired
    GroupClassDao groupClassDao;

    @Autowired
    MembershipDao membershipDao;
    @Autowired
    TransactionDao transactionDao;
    @Test
    void selectAllUser() {
        List<User> users = userDao.selectList(null);
        System.out.println(users);
    }
    @Test
    void test()
    {
        QueryWrapper<Transaction> queryWrapper=new QueryWrapper<>();
        queryWrapper.groupBy("transaction_type");
        queryWrapper.select("sum(amount) as amount,transaction_type");
        List<Transaction> transactions = transactionDao.selectList(queryWrapper);
    }
}
