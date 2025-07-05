package dao;

import dao.strategy.StatementStrategy;
import domain.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

    private final JdbcContext jdbcContext;

    public UserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException {
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {//익명 내부 클래스는 구현하는 인터페이스를 생성자처럼 이용해서 오브젝트로 만든다.
            //클래스 필드인 jdvcContext를 사용해서 명시적으로 보여주기위해 this.를 사용
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");

                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        }
        );

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

    public void deleteAll()throws SQLException {//중첩 클래스, 익명 내부 클래스
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("delete from users");
            }
        });
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
