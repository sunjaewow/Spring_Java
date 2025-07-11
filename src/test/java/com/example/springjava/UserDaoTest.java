package com.example.springjava;

import dao.UserDao;
import domain.Level;
import domain.User;
import factory.DaoFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = DaoFactory.class)//스프링 애플리케이션 컨텍스트는 초기화할 때 자기 자신도 빈으로 등록한다.
public class UserDaoTest {

    UserDao dao;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/spring_java_test", "root", "fpdlswj365", true);
        dao = new UserDao(dataSource);

        this.user1 = new User("aa", "aa1", "aa2", Level.BASIC, 1, 0);
        this.user2 = new User("bb", "bb1", "bb2", Level.SILVER, 55, 10);
        this.user3 = new User("cc", "cc1", "cc2", Level.GOLD, 100, 40);
    }

    @AfterEach
    public void delete() {
        dao.deleteAll();
    }

//
//    @Test
//    public void addAndGet() {
//
//        User getUser1 = dao.get(user1.getId());
//        checkSameUser(getUser1, user1);
//
//        User getUser2 = dao.get(user2.getId());
//        checkSameUser(getUser2, user2);
//    }

    @Test
    public void getUserFailure() {

        assertThatThrownBy(() -> dao.get("unknown")).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1);

        user1.setName("aaaa");
        user1.setPassword("aaaaa2");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);

        dao.add(user2);
        User user2update = dao.get(user2.getId());
        checkSameUser(user2, user2update);

    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }
}
