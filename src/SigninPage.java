import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class SigninPage extends JFrame {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private JPanel panel;
    JLabel message = new JLabel(""); //회원가입 안내 확인창
    JButton okBtn = new JButton("OK");; //확인창 버튼

    boolean signTF;

    public SigninPage() {
        // JFrame 타이틀 설정
        setTitle("YourName");

        // 배경 이미지를 위한 JPanel 생성
        panel = new JPanel() {
            // paintComponent 메소드를 오버라이딩하여 배경 이미지를 설정
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("src/img/YOURNAME_SIGNIN.png");
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

        JPanel msgPanel = new JPanel();

        msgPanel.setLayout(new FlowLayout());
        msgPanel.setBounds(350, 300, 500, 200);

        JButton SigninBtn = new JButton("");
        SigninBtn.setBounds(370, 500,150, 50);
        SigninBtn.setOpaque(false);
        SigninBtn.setContentAreaFilled(false);
        SigninBtn.setBorderPainted(false);

        SigninBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = NameText.getText();
                String password = PasswordText.getText();
                signTF = signInUser(name, password);
                message.setText("");//message 라벨 내용 리셋
                if (signTF == true){ //만약 회원가입이 성공(true)라면
                    System.out.println(signTF);
                    message = new JLabel("회원가입에 성공했습니다! 로그인 화면으로 돌아갑니다."); //라벨 내용을 성공 내용을 바꿈
                } else {
                    System.out.println(signTF);
                    message = new JLabel("이미 존재하는 아이디입니다.");
                }
                okBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // cancel 버튼 눌렀을 때 처리할 내용
                        if(signTF == true){
                            new LoginPage();
                        } else {
                            msgPanel.setVisible(false);
                        }

                    }
                });
                message.setBounds(10, 500, 100, 100);
                okBtn.setBounds(10, 100, 10, 10);
                msgPanel.add(message);
                msgPanel.add(okBtn);
                msgPanel.setVisible(true);
            }
        });
        msgPanel.setVisible(false);


//        JPanel panel = new JPanel(new BorderLayout());


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

        JButton loginBtn = new JButton("");
        loginBtn.setBounds(980, 670, 140, 50);
        loginBtn.setOpaque(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
                dispose();
            }

        });


        // JPanel을 JFrame에 추가

        add(NameText);
        add(PasswordText);
        add(SigninBtn);
        add(cancelBtn);
        add(loginBtn);
        add(msgPanel);
        msgPanel.getParent().setComponentZOrder(msgPanel, 0);
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
    private static boolean signInUser(String name, String password) {
        // DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/rabbitScoreDB"; // DB 접속 URL
        String user = "root"; // DB 접속 계정
        String passwd = "@summer0573"; // DB 접속 비밀번호

        // DB 연결
        Connection conn;
        Statement stmt;
        ResultSet rs;

        try{
            conn = DriverManager.getConnection(url, user, passwd);
            stmt = conn.createStatement();
            stmt.executeUpdate(" INSERT INTO User_table(name, userPassword)" +
                    "VALUES ('" + name + "', '" + password + "');");//테이블에 스코어를 추가 시키는 큐리문
            System.out.println("데이터 저장 성공");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("데이터 저장 실패");
            return false;
        }

    }

    private static boolean isValidUser(String name, String password) {
        // DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/rabbitScoreDB"; // DB 접속 URL
        String user = "root"; // DB 접속 계정
        String passwd = "@summer0573"; // DB 접속 비밀번호

        // DB 연결
        Connection conn;
        Statement stmt;
        ResultSet rs;
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
        SigninPage frame = new SigninPage();
    }
}
