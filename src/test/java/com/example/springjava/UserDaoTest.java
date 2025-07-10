package com.example.springjava;

import dao.UserDao;
import domain.User;
import factory.DaoFactory;
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

    @BeforeEach
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/spring_java_test", "root", "fpdlswj365", true);
        dao = new UserDao(dataSource);

    }

    @Test
    public void addAndGet() {

        User user = new User("chltjswo", "최선재", "good");

        dao.add(user);

        User user2 = dao.get(user.getId());

        dao.deleteAll();
        int count = dao.getCount();

        assertThat(user2.getId()).isEqualTo(user.getId());
        assertThat(user2.getName()).isEqualTo(user.getName());
        assertThat(user2.getPassword()).isEqualTo(user.getPassword());
//        assertThat(user2.getLevel()).isEqualTo(Level.BASIC);
//        assertThat(user2.getLogin()).isEqualTo(3);
//        assertThat(user2.getRecommend()).isEqualTo(0);
        assertThat(count == 0).isTrue();
    }

    @Test
    public void getUserFailure() {

        assertThatThrownBy(() -> dao.get("unknown")).isInstanceOf(EmptyResultDataAccessException.class);
    }


    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId().equals(user2.getId())).isTrue();
        assertThat(user1.getName().equals(user2.getName())).isTrue();
        assertThat(user1.getPassword().equals(user2.getPassword())).isTrue();
//        assertThat(user1.getLevel().equals(user2.getLevel())).isTrue();
//        assertThat(user1.getLogin()==(user2.getLogin())).isTrue();
//        assertThat(user1.getRecommend()==(user2.getRecommend())).isTrue();
    }
}
