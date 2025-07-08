package com.example.springjava;

import dao.UserDao;
import domain.User;
import factory.DaoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Test
    public void addAndGet() {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("chltjswo");
        user.setName("최선재");
        user.setPassword("good");

        dao.add(user);

        User user2 = dao.get(user.getId());

        assertThat(user2.getName()).isEqualTo(user.getName());
        assertThat(user2.getPassword()).isEqualTo(user.getPassword());

    }


    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId().equals(user2.getId())).isTrue();
        assertThat(user1.getName().equals(user2.getName())).isTrue();
        assertThat(user1.getPassword().equals(user2.getPassword())).isTrue();
        assertThat(user1.getLevel().equals(user2.getLevel())).isTrue();
        assertThat(user1.getLogin()==(user2.getLogin())).isTrue();
        assertThat(user1.getRecommend()==(user2.getRecommend())).isTrue();
    }
}
