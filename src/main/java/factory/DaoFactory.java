package factory;

import com.zaxxer.hikari.HikariDataSource;
import dao.*;
import dao.connection.ConnectionMaker;
import dao.connection.CountingConnectionMaker;
import dao.connection.NConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {

        return new UserDao(new JdbcContext());
    }

    @Bean
    public DataSource dataSource() {

        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/spring_java");
        ds.setUsername("root");
        ds.setPassword("fpdlswj365");
        return ds;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new NConnectionMaker();
    }
}
