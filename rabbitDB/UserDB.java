package rabbitDB;

import java.sql.*;
//유저 회원 정보 테이블에 저장하는 코드
public class UserDB {
    public UserDB(){
        String url = "jdbc:mysql://localhost:3306/rabbitScoreDB";
        String userName = "root";
        String password = "@summer0573";

//        String name; //사용자 닉네임
//        String userPassword; //사용자 비밀번호

        new UserDBHelper(); // 유저 테이블이 없을 경우 유저 정보 저장 테이블 생성함

        //임시
        String name = "김마스으"; //사용자 닉네임
        String userPassword = "asdfasdf"; //사용자 비밀번호
        //이름은 20자가 넘지 않도록, 비밀번호는 50자가 넘지 않도록

        try{
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(" INSERT INTO User_table(name, userPassword)" +
                    "VALUES ('" + name + "', '" + userPassword + "');");//테이블에 스코어를 추가 시키는 큐리문
            System.out.println("데이터 저장 성공");
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("데이터 저장 실패");
        }

    }
    public static void main(String[] args) throws SQLException {
        new UserDB();
    }

}
