package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {

    @Autowired
    ApplicationContext context;

    UserDao userDao;
    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp(){
        this.userDao = context.getBean("awsUserDao", UserDao.class);
        this.user1 = new User("20","Kwon","1123");
        this.user2 = new User("21","so","12334");
        this.user3 = new User("22","yeon","4321");
    }

    @Test
    @DisplayName("add, get, deleteAll 테스트")
    void addAndGet() throws SQLException {
        userDao.deleteAll();
        assertEquals(0,userDao.getCount());

        userDao.add(user1);

        User selectedUser = userDao.get(user1.getId());
        assertEquals(user1.getName(),selectedUser.getName());
        assertEquals(user1.getPassword(),selectedUser.getPassword());
    }

    @Test
    @DisplayName("getCount테스트")
    void count() throws SQLException {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        userDao.add(user1);
        assertEquals(1, userDao.getCount());
        userDao.add(user2);
        assertEquals(2, userDao.getCount());
        userDao.add(user3);
        assertEquals(3, userDao.getCount());
    }

    @Test
    @DisplayName("findById 없는 경우 예외 처리")
    void findById() {
        assertThrows(EmptyResultDataAccessException.class, ()->{
            userDao.get("30");
        });
    }

    @Test
    @DisplayName("모든 User List로 받기")
    void getAllTest() throws SQLException {
        userDao.deleteAll();
        List<User> users = userDao.getAll();
        assertEquals(0, users.size());

        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
        users = userDao.getAll();
        assertEquals(3, users.size());
    }

}