package factory;

import dao.ConnectionMaker;
import dao.NUserDao;
import dao.UserDao;

public class DaoFactory {
    public UserDao userDao() {
        ConnectionMaker connectionMaker = new NUserDao();
        return new UserDao(connectionMaker);
    }
}
