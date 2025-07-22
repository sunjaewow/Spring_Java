package com.example.springjava;

import config.AppConfig;
import dao.UserDao;
import domain.Level;
import domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import service.UserServiceImpl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static service.UserServiceImpl.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class UserServiceImplTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    UserServiceImpl userServiceImpl;

    UserDao dao;

    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("sunjae", "선재", "email1","p1", Level.BASIC, MIN_LOGCOUNT_FOR_SLIVER -1, 0),
                new User("jihye", "지혜", "email2","p2", Level.BASIC, MIN_LOGCOUNT_FOR_SLIVER, 0),
                new User("junngwook", "정욱", "email3","p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
                new User("sumin", "수민","email4", "p4", Level.SILVER, 60,MIN_RECCOMEND_FOR_GOLD),
                new User("yoonjung", "윤정","email5", "p5", Level.GOLD, 100, 100)
        );

        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/spring_java_test", "root", "fpdlswj365", true);
        dao = new UserDao(dataSource);
        userServiceImpl = new UserServiceImpl();
        userServiceImpl.setTransactionManager(transactionManager);
        userServiceImpl.setUserService(dao);
    }

    @AfterEach
    public void after() {
        dao.deleteAll();
    }

    @Test
    public void bean() {
        assertThat(this.userServiceImpl).isNotNull();
    }

    @Test
    public void add() {
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userServiceImpl.add(userWithLevel);
        userServiceImpl.add(userWithoutLevel);

        User userWithLevelRead = dao.get(userWithLevel.getId());
        User userWithoutLevelRead = dao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    public void upgradeLevels() throws SQLException {
        for (User user : users) {
            dao.add(user);
        }
        userServiceImpl.upgradeLevels();
        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }


    private void checkLevel(User user, boolean upgraded) {
        User upgradeUser = dao.get(user.getId());
        if (upgraded) {
            assertThat(upgradeUser.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(upgradeUser.getLevel()).isEqualTo(user.getLevel());
        }
    }
}
