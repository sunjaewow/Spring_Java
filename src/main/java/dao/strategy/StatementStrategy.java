package dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;
    //단점 1.DAO 메소드마다 새로운 구현 클래스를 만들어야 한다. 파일이 많아진다.
    //    2.전달한 User와 같은 부가적인 정보가 있는 경우, 이를 위해 오브젝트를 전달받는 생성자와 이를 저장해둘 인스턴스 변수를 번거롭게 만들어야 한다는 점.
}
