package dao;

import domain.Level;
import domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
//        jdbcContext.executeUpdateQuery("insert into users(id, name, password) values (?,?,?)", user.getId(), user.getName(), user.getPassword());
        this.jdbcTemplate.
                update("insert into users(id, name,email, password, level, login, recommend) values (?,?,?,?,?,?,?)",
                        user.getId(), user.getName(),user.getEmail(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }


    public void deleteAll() {//중첩 클래스, 익명 내부 클래스
//        jdbcContext.executeUpdateQuery("delete from users"); 스프링은 jdbc를 이용하는 dao에서 사용할 수 있도록 준비된 다양한 템플릿과 콜백을 제공
        //할 뿐만아니라 자주 사용되는 패턴을 가진 콜백은 다시 템플릿에 결합시켜서 간단한 메소드 호출만으로 사용이 가능하도록 만들어져 있음.
        this.jdbcTemplate.update("delete from users");//그래서 이렇게 간단하게 메서드를 이용해서 가능
    }

    public int getCount(){
//        return jdbcContext.executeQuery("select count(*)from users");
//        return this.jdbcTemplate.query(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                return con.prepareStatement("select count(*)from users");
//            }
//        }, new ResultSetExtractor<Integer>() {
//            @Override
//            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//                rs.next();
//                return rs.getInt(1);
//            }
//        });
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);

    }

    public User get(String id){
//        return jdbcContext.executeQuery(id,"select  * from users where id=?");
        return this.jdbcTemplate.queryForObject("select * from users where id=?",//조회 결과가 없는 경우 예외를 처리해줘야하는데
                // queryforobject는 예외를 던지도록 만들어져 있다. 미친
                new Object[]{id}, userMapper);
    }

    public void update(User user) {
        this.jdbcTemplate.update("update users set name = ?,email=?, password = ?, level=?, login=?, recommend=? where id=?"
                , user.getName(), user.getEmail(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", userMapper);
    }

    private RowMapper<User> userMapper = new RowMapper<User>() {//중복 코드 추출
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        }
    };


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
