package rabbitDB;

import java.sql.*;

//데이터 베이스와 점수 저장 테이블이 없을시 생성하는 코드
public class ScoreDBHelper {
    public ScoreDBHelper() {
        String url = "jdbc:mysql://127.0.0.1/?useSSL=false&user=root&password=@summer0573";
        String userName = "root";
        String password = "@summer0573";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("드라이버 연결 성공!");
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement stmt = connection.createStatement();
            String createSql = "CREATE DATABASE IF NOT EXISTS `rabbitScoreDB`;";  // DB가 없다면 DB를 만들어라

            stmt.executeUpdate(createSql);
            url="jdbc:mysql://localhost/rabbitScoreDB?serverTimezone=UTC";
            connection = DriverManager.getConnection(url, userName,password);
            stmt = connection.createStatement();
            createSql = "CREATE TABLE IF NOT EXISTS rabbit_table(" +
                    "name varchar(20) NOT NULL," +
                    "first_score int, first_clear  tinyint(1)," +
                    "second_score int, second_clear tinyint(1)," +
                    "FOREIGN KEY (`name`) REFERENCES `User_table` (`name`)," +
                    ");";
            //^tableㅇㅣ 없다면 table를 만들어라
            stmt.executeUpdate(createSql);
            stmt.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            System.out.print("생성 실패");
        }
    }
    public static void main(String[] args) throws SQLException {
        new ScoreDBHelper();


    }
}