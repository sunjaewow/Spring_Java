package dao;

import dao.strategy.StatementStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private final DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }finally {
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

//    public void executeSql(String sql) throws SQLException {//익명 내부 클래스
//        workWithStatementStrategy(new StatementStrategy() {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                return c.prepareStatement(sql);//바뀌는 부분 분리.
//            }
//        });
//    }

    public void executeSql(String sql, String...params) throws SQLException {//가변인자 사용
        workWithStatementStrategy(new StatementStrategy() {//익명 내부 클래스는 구현하는 인터페이스를 생성자처럼 이용해서 오브젝트로 만든다.
                                      //클래스 필드인 jdvcContext를 사용해서 명시적으로 보여주기위해 this.를 사용
                                      @Override
                                      public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                                          PreparedStatement ps = c.prepareStatement(sql);

                                          for (int i = 0; i < params.length; i++) {
                                              ps.setString(i + 1, params[i]);
                                          }
                                          return ps;
                                      }
                                  }
        );
    }
}
