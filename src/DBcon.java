import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;
public class DBcon {

    private Connection con;

    public Connection getCon() {
        return con;
    }

    // DB 활용을 위한 기본 작업 > jdbc Driver로 불러오기
    public DBcon() {
        try {
            String url = "jdbc:mysql://localhost:3306/ham_schema";
            String user = "root";
            String passwd = "mirim";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection(url,user, passwd);
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