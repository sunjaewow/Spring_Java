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
            boolean changed = false;

            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else if (user.getLevel() == Level.GOLD) {
                changed = false;
            }else
                changed = false;

            if (changed) {
                userDao.update(user);//레벨 변경이 있는 경우에만 update()
            }
        }
    }
}
