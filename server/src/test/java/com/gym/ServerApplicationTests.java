package com.gym;

import com.gym.controller.MembershipController;
import com.gym.controller.ScheduleController;
import com.gym.damain.GroupClass;
import com.gym.damain.Membership;
import com.gym.damain.User;
import com.gym.dao.GroupClassDao;
import com.gym.dao.MembershipDao;
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
    @Test
    void selectAllUser() {
        List<User> users = userDao.selectList(null);
        System.out.println(users);
    }
    @Test
    void test()
    {

    }
}
