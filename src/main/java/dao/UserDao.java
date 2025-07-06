package dao;

import dao.strategy.StatementStrategy;
import domain.User;

import java.sql.*;

public class UserDao {

    private final JdbcContext jdbcContext;

    public UserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException {
        jdbcContext.executeSql("insert into users(id, name, password) values (?,?,?)", user.getId(), user.getName(), user.getPassword());

    }

    public void deleteAll()throws SQLException {//중첩 클래스, 익명 내부 클래스
        jdbcContext.executeSql("delete from users");
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id=?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();
        return user;
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");//변하는 부분 -> 변하는 부분을 따로 추출해도 재사용 할 필요가 없음 반대가됨.

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            throw e;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {

                }
            }
        }

    }


//    public void deleteAll()throws SQLException {
//        StatementStrategy st = new DeleteAllStatement();//전략 오브젝트 생성.
//        jdbcContextWithStatementStrategy(st);//컨텍스트 호출, 전략 오브젝트 전달.
//    }
//
//    public void deleteAll() throws SQLException {
//        Connection c = null;
//        PreparedStatement ps = null;
//
//        try {
//            c = dataSource.getConnection();
//
//            StatementStrategy statementStrategy = new DeleteAllStatement();
//            ps = statementStrategy.makePreparedStatement(c);//전략패턴은 컨텍스트는 그대로 유지되면서 전략을 바꿔 쓸 수 있는건데 이렇게 컨텍스트 안에서 이미 구체적인 전략 클래스가 있다면 안됨.
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            throw e;
//        }finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//
//                }
//            }
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//
//                }
//            }
//        }

//    }

//    abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
}
