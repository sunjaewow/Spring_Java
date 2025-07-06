//package dao.strategy;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class getCountStatement implements StatementStrategy{
//    @Override
//    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//        return c.prepareStatement("select count(*)from users");
//    }
//}
