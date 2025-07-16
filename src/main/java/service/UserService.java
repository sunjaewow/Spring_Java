package service;

import dao.UserDao;
import domain.Level;
import domain.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    UserDao userDao;
    DataSource dataSource;

    public static final int MIN_LOGCOUNT_FOR_SLIVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    public void setUserService(UserDao userDao, DataSource dataSource) {
        this.userDao = userDao;
        this.dataSource = dataSource;
    }


    public void upgradeLevel(User user) {

        Level nextLevel = user.getLevel().nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(user.getLevel() + "은 업그레이드가 불가능합니다.");
        } else {
            user.setLevel(nextLevel);
            userDao.update(user);
        }
    }

    public void upgradeLevel() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();//트랜잭션 동기화 매니저를 이용해 동기화 작업을 초기화한다.
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);//db 커넥션을 생성하고, 트랜잭션을 시작한다. 이후의 dao 작업은 모두 여기서 시작한 트랜잭션 안에서 진행된다.
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            c.commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        }finally {
            DataSourceUtils.releaseConnection(c, dataSource);//db connection을 안전하게 닫는다.
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
            //동기화 작업 종료 및 정리
        }
    }

    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC -> {
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SLIVER);
            }
            case SILVER -> {
                return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
            }
            case GOLD -> {
                return false;
            }
            default -> {
                throw new IllegalArgumentException("Unknown Level:" + currentLevel);
            }
        }
    }

    public void add(User userWithoutLevel) {
        if (userWithoutLevel.getLevel() == null) {
            userWithoutLevel.setLevel(Level.BASIC);
        }
        userDao.add(userWithoutLevel);
    }

}
