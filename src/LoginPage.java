import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class LoginPage extends JFrame {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private JPanel panel;

    public LoginPage() {
        // JFrame 타이틀 설정
        setTitle("YourName");

        // 배경 이미지를 위한 JPanel 생성
        panel = new JPanel() {
            // paintComponent 메소드를 오버라이딩하여 배경 이미지를 설정
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("src/img/YOURNAME_LOGIN.png");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // JPanel 크기 및 레이아웃 설정
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        panel.setLayout(new FlowLayout());
        panel.setLayout(null);

        // loginText 생성
        JTextField NameText = new JTextField(20);
        NameText.setBounds(350, 235, 400, 50);
//        loginText.setOpaque(false);
        NameText.setBorder(BorderFactory.createEmptyBorder());

        JTextField PasswordText = new JTextField(20);
        PasswordText.setBounds(350, 385, 400, 50);

        PasswordText.setBorder(BorderFactory.createEmptyBorder());

        JButton LoginBtn = new JButton("");
        LoginBtn.setBounds(370, 500,150, 50);
        LoginBtn.setOpaque(false);
        LoginBtn.setContentAreaFilled(false);
        LoginBtn.setBorderPainted(false);

        LoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = NameText.getText();
                String password = PasswordText.getText();
                if (isValidUser(name, password)) {
                    System.out.println("로그인 되었습니다.");
                } else {
                    System.out.println("실패.");
                }
            }
        });

        JButton cancelBtn = new JButton("");
        cancelBtn.setBounds(630, 500,170, 50);
        cancelBtn.setOpaque(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Main 클래스의 main 메소드를 호출하여 프로그램 종료
                Main.main(new String[] {});
                // 현재 로그인 창은 닫아주어야 함
                dispose();
            }
        });

        JButton SigninBtn = new JButton("");
        SigninBtn.setBounds(980, 670, 140, 50);
        SigninBtn.setOpaque(false);
        SigninBtn.setContentAreaFilled(false);
        SigninBtn.setBorderPainted(false);

        SigninBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SigninPage signinPage = new SigninPage();
                signinPage.setVisible(true);
                dispose();
            }

        });


        // JPanel을 JFrame에 추가
        add(NameText);
        add(PasswordText);
        add(LoginBtn);
        add(cancelBtn);
        add(SigninBtn);
        add(panel);




        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // JFrame 크기 설정
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // JFrame을 화면에 표시
        setVisible(true);
    }
    private static boolean isValidUser(String name, String password) {
        // DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/rabbitScoreDB"; // DB 접속 URL
        String user = "root"; // DB 접속 계정
        String passwd = "mirim"; // DB 접속 비밀번호

        // DB 연결
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver"); // JDBC 드라이버 로드
            conn = DriverManager.getConnection(url, user, passwd); // DB 접속
            stmt = conn.createStatement();

            // 사용자 인증 쿼리
            String query = "SELECT * FROM users WHERE name='" + name + "' AND password='" + password + "'";
            rs = stmt.executeQuery(query);
            return rs.next(); // 결과가 존재하면 true, 그렇지 않으면 false 반환

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        LoginPage frame = new LoginPage();
    }
}
