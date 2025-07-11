package com.example.springjava;

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
import service.UserService;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    UserService userService;

    UserDao dao;

    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("sunjae", "선재", "p1", Level.BASIC, 49, 0),
                new User("jihye", "지혜", "p2", Level.BASIC, 50, 0),
                new User("junngwook", "정욱", "p3", Level.SILVER, 60,29),
                new User("sumin", "수민", "p4", Level.SILVER, 60,30),
                new User("yoonjung", "윤정", "p5", Level.GOLD, 100, 100)
        );

        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/spring_java_test", "root", "fpdlswj365", true);
        dao = new UserDao(dataSource);
        userService = new UserService();
        userService.setUserService(dao);
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
    public void upgradeLevels() {
        for (User user : users) {
            dao.add(user);
        }
        userService.upgradeLevels();
        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
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

    private void checkLevel(User user, Level expectedLeve) {
        User userUpdate = dao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLeve);
    }
}
