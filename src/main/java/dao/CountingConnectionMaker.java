package dao;

import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker{

    @Getter
    int counter = 0;

    private final ConnectionMaker connectionMaker;

    public CountingConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return connectionMaker.getConnection();

    }

}
