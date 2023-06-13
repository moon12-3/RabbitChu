import javax.naming.Name;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class LoginPage extends JFrame {
    Toolkit tk = Toolkit.getDefaultToolkit();
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private JPanel panel;
    JLabel message = new JLabel(""); //회원가입 안내 확인창
    JButton okBtn = new JButton("OK");; //확인창 버튼
    public Font font;
    boolean signTF;
    Clip ost;

    public LoginPage() {
        setTitle("YourName");

        ost = Sound("src/bgm/intro.wav", true);

        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - FRAME_WIDTH / 2);
        int ypos = (int) (screen.getHeight() / 2 - FRAME_HEIGHT / 2);
        setLocation(xpos, ypos);

        // JFrame 크기 설정
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

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

        // TextField 폰트 설정
        // 외부 폰트 사용하기
        try {
            InputStream inputStream = new BufferedInputStream(
                    new FileInputStream("src/font/dunggeunmo.ttf"));

            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(20f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // loginText 생성
        JTextField NameText = new JTextField("이름을 입력하세요");
        NameText.setBounds(350, 235, 400, 50);
//        loginText.setOpaque(false);
        NameText.setBorder(BorderFactory.createEmptyBorder());
        NameText.setFont(font);
        NameText.setForeground(Color.GRAY);
        NameText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (NameText.getText().equals("이름을 입력하세요")) {
                    NameText.setText("");
                    NameText.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (NameText.getText().isEmpty()) {
                    NameText.setForeground(Color.GRAY);
                    NameText.setText("이름을 입력하세요");
                }
            }
        });

        JTextField PasswordText = new JTextField("비밀번호를 입력하세요");
        PasswordText.setBounds(350, 385, 400, 50);
        PasswordText.setBorder(BorderFactory.createEmptyBorder());
        PasswordText.setFont(font);
        PasswordText.setForeground(Color.GRAY);
        PasswordText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (PasswordText.getText().equals("비밀번호를 입력하세요")) {
                    PasswordText.setText("");
                    PasswordText.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (PasswordText.getText().isEmpty()) {
                    PasswordText.setForeground(Color.GRAY);
                    PasswordText.setText("비밀번호를 입력하세요");
                }
            }
        });

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new FlowLayout());
        msgPanel.setBounds(350, 300, 500, 200);

        JButton LoginBtn = new JButton("");
        LoginBtn.setBounds(370, 500, 150, 50);
        LoginBtn.setOpaque(false);
        LoginBtn.setContentAreaFilled(false);
        LoginBtn.setBorderPainted(false);

        LoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = NameText.getText();
                String password = PasswordText.getText();
                signTF = isValidUser(name, password);
                msgPanel.setLayout(null);
                message.setText("");//message 라벨 내용 리셋
                if (signTF == true) {
                    System.out.println("로그인 되었습니다.");
                    message = new JLabel("<html><body><center>로그인되었습니다.<br>" +
                            "<br>게임을 실행합니다.<br></center></body></html>", JLabel.CENTER); //라벨 내용을 성공 내용을 바꿈
                    message.setBounds(100, 20, 300, 100);
                } else {
                    System.out.println("실패.");
                    message = new JLabel("회원정보가 틀렸습니다.");
                    message.setBounds(180, 20, 150, 100);
                }
                okBtn.setBounds(215, 120, 70, 40);
                okBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // cancel 버튼 눌렀을 때 처리할 내용
                        if (signTF == true) {
                            ost.stop();
                            dispose();
                            new Frame_make();
                            msgPanel.setVisible(false);
                        } else {
                            msgPanel.setVisible(false);
                        }

                    }
                });
                msgPanel.add(message);
                msgPanel.add(okBtn);
                msgPanel.setVisible(true);
            }
        });
        msgPanel.setVisible(false);

        JButton cancelBtn = new JButton("");
        cancelBtn.setBounds(630, 500, 170, 50);
        cancelBtn.setOpaque(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Main 클래스의 main 메소드를 호출하여 프로그램 종료
                ost.stop();
                Main.main(new String[]{});
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
                ost.stop();
                SigninPage signinPage = new SigninPage();
                signinPage.setVisible(true);
                dispose();
            }

        });

        // JPanel을 JFrame에 추가
        add(msgPanel);
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

        // JFrame을 화면에 표시
        setVisible(true);
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
        String checkingStr;
        boolean select = false;
        try {
            conn = DriverManager.getConnection(url, user, passwd); // DB 접속
            stmt = conn.createStatement();
            // 사용자 인증 쿼리
            checkingStr = "SELECT * FROM user_table WHERE name='" + name + "' AND userPassword='" + password + "'";
            rs = stmt.executeQuery(checkingStr);
            while (rs.next()) {
                if (password.equals(rs.getString("userPassword"))) {
                    System.out.println("로그인 성공");
                    System.out.println(password.equals(rs.getString("userPassword")));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("유저 존재하지 않음 " + select);
            return false;
        }
        return false;
    }

    public Clip Sound(String file, boolean Loop){ //사운드 재생용 메소드
        Clip clip = null;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
            clip = AudioSystem.getClip();
            clip.open(ais);

            clip.start();
            if (Loop) clip.loop(-1);   // 계속 재생할 것인지

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    public static void main(String[] args) {
        LoginPage frame = new LoginPage();
    }
}
