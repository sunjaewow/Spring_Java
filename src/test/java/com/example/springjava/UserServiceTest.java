package com.example.springjava;

import dao.UserDao;
import domain.Level;
import domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import service.UserService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static service.UserService.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    UserService userService;

    UserDao dao;

    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("sunjae", "선재", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SLIVER -1, 0),
                new User("jihye", "지혜", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SLIVER, 0),
                new User("junngwook", "정욱", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
                new User("sumin", "수민", "p4", Level.SILVER, 60,MIN_RECCOMEND_FOR_GOLD),
                new User("yoonjung", "윤정", "p5", Level.GOLD, 100, 100)
        );

        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/spring_java_test", "root", "fpdlswj365", true);
        dao = new UserDao(dataSource);
        userService = new UserService();
        userService.setUserService(dao, dataSource);
    }

    @AfterEach
    public void after() {
        dao.deleteAll();
    }

    @Test
    public void bean() {
        assertThat(this.userService).isNotNull();
    }

    @Test
    public void add() {
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

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
        userService.upgradeLevel();
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
