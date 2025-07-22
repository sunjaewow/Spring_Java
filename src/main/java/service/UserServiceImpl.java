package service;

import dao.UserDao;
import domain.Level;
import domain.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.List;

@Component
public class UserServiceImpl implements UserService{

    UserDao userDao;

    PlatformTransactionManager transactionManager;

    public static final int MIN_LOGCOUNT_FOR_SLIVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    public void setUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Override
    public void add(User userWithoutLevel) {
        if (userWithoutLevel.getLevel() == null) {
            userWithoutLevel.setLevel(Level.BASIC);
        }
        userDao.add(userWithoutLevel);
    }

    @Override
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
                userDao.update(user);
                sendUpgradeEmail(user);
            }
        }
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

//    public void upgradeLevelsInternal() throws SQLException {
////        TransactionSynchronizationManager.initSynchronization();//트랜잭션 동기화 매니저를 이용해 동기화 작업을 초기화한다.
////        Connection c = DataSourceUtils.getConnection(dataSource); jdbc일 때
////        c.setAutoCommit(false);//db 커넥션을 생성하고, 트랜잭션을 시작한다. 이후의 dao 작업은 모두 여기서 시작한 트랜잭션 안에서 진행된다.
////        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);//jdbc 트랜잭션 추상 오브젝트 생성
////        PlatformTransactionManager txManager = new JtaTransactionManager();//글로벌 트랜잭션으로 변경
////        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());//트랜잭션 추상화
//        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
//        //각각 다른 트랜잭션을 이용하는경우
//        try {
//            upgradeLevelsInternal();
////            c.commit();
//            this.transactionManager.commit(status);
//        } catch (Exception e) {
////            c.rollback();
//            this.transactionManager.rollback(status);
//            throw e;
//        }
////        finally {
////            DataSourceUtils.releaseConnection(c, dataSource);//db connection을 안전하게 닫는다.
////            TransactionSynchronizationManager.unbindResource(this.dataSource);
////            TransactionSynchronizationManager.clearSynchronization();
////            //동기화 작업 종료 및 정리
////        }
//    }

    private void sendUpgradeEmail(User user) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이"+user.getLevel().name());

        mailSender.send(mailMessage);
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

}
