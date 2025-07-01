package com.example.springjava;

import dao.ConnectionMaker;
import dao.DUserDao;
import dao.UserDao;
import domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class SpringJavaApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ConnectionMaker connectionMaker = new DUserDao();

        UserDao dao = new UserDao(connectionMaker);

        User user = new User();
        user.setId("chltjswo");
        user.setName("최선재");
        user.setPassword("good");

        dao.add(user);

        System.out.println(user.getId()+"등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());

        System.out.println(user2.getPassword());

        System.out.println(user2.getId()+"조회 성공");

    }

}
