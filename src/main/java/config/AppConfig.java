package config;

import dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import service.UserService;
import service.UserServiceImpl;
import service.UserServiceTx;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3306/spring_java_test",
                "root",
                "fpdlswj365",
                true
        );
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean
    UserService userService() {
        return new UserServiceImpl(new UserDao(dataSource()));
    }
}
