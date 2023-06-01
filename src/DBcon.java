import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import rabbitDB.*;

public class DBcon {

    private Connection con;

    public Connection getCon() {
        return con;
    }

    // DB 활용을 위한 기본 작업 > jdbc Driver로 불러오기
    public DBcon() {
        new DBHelper();
        new UserDBHelper();
        new ScoreDBHelper();

        try {
            String url = "jdbc:mysql://localhost:3306/rabbitScoreDB";
            String userName = "root";
            String password = "@summer0573";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("드라이버 연결 성공!");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e);
                System.out.print("생성 실패");
            }
            con = DriverManager.getConnection(url, userName, password);
            System.out.println("DB연결 성공");
        } catch (SQLException e) {
            System.out.println("DB연결 실패");
            System.out.print("사유 : " );
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DBcon();
    }

}