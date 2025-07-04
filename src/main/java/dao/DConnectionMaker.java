package dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker{

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        //다른 connection 생성코드
        return null;
    }
}
