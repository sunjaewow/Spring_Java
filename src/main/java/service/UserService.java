package service;

import dao.UserDao;
import domain.Level;
import domain.User;

import java.util.List;

public class UserService {

    UserDao userDao;

    public void setUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    public void upgradeLevel(User user) {
        Level nextLevel = user.getLevel().nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(user.getLevel() + "은 업그레이드가 불가능합니다.");
        } else {
            userDao.update(user);
        }
    }

    public void upgradeLevel() {
    }

    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC -> {
                return (user.getLogin() >= 50);
            }
            case SILVER -> {
                return (user.getRecommend() >= 30);
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
