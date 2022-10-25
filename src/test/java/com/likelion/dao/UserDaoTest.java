package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {


    @Test
    void addAndGet() throws SQLException {
        UserDao userDao = new UserDaoFactory().awsUserDao();
        userDao.deleteAll();
        assertEquals(0,userDao.getCount());

        User user1 = new User("8","kyeongrok","12345");
        userDao.add(user1);

        User selectedUser = userDao.get("8");
        assertEquals("kyeongrok",selectedUser.getName());
    }
}