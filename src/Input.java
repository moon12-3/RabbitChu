import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Input extends JFrame {
    int width, height;
    Toolkit tk = Toolkit.getDefaultToolkit();

    Font font;
    Color colorBrown = new Color(0x312500);

    String name;
    int score;
    JLabel labelGetScore;
    Clip ost;

    Image backGround = new ImageIcon("src/img/back_input.png").getImage();
    ImageIcon iconSave = new ImageIcon("src/img/btn_to_save.png");

    public void init() throws IOException, FontFormatException { // 컴포넌트 세팅
        width = 1200;
        height = 800;

    }

    public void setScore(int score) {
        this.score = score;
    }

    public Input() throws IOException, FontFormatException {
        super("input"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();

        ost = Sound("src/bgm/intro.wav", true);

        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);

        this.setSize(width, height);
        String newScor = Integer.toString(score);

        MyPanel panel = new MyPanel();
        panel.setLayout(null);

        // 외부 글꼴 적용
        try {
            InputStream inputStream = new BufferedInputStream(
                    new FileInputStream("src/font/dunggeunmo.ttf"));

            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // 버튼, 라벨 추가 및 설정
        JLabel labelName = new JLabel("NAME");
        labelName.setFont(font.deriveFont(Font.BOLD, 43));
        labelName.setForeground(colorBrown);
        labelName.setHorizontalAlignment(JLabel.CENTER);
        labelName.setBounds(300, 295, 300, 70);

        // 사용자에게 이름 받아오기
        JTextField textName = new JTextField();
        textName.setBounds(560, 305, 230, 50);
        textName.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        textName.setFont(font.deriveFont(Font.BOLD, 30));
        textName.setForeground(colorBrown);
        name = textName.getText();

        JLabel labelScore = new JLabel("SCORE");
        labelScore.setFont(font.deriveFont(Font.BOLD, 43));
        labelScore.setForeground(colorBrown);
        labelScore.setHorizontalAlignment(JLabel.CENTER);
        labelScore.setBounds(300, 415, 300, 70);

        // 게임 점수 전달
        labelGetScore = new JLabel("");
        labelGetScore.setBounds(560, 425, 230, 50);
        labelGetScore.setFont(font.deriveFont(Font.BOLD, 30));

        JButton btnSave = new JButton(iconSave);
        btnSave.setBounds(485, 530, 230, 80);
        btnSave.setBorderPainted(false);
        btnSave.setContentAreaFilled(false);
        btnSave.setFocusPainted(false);

        JLabel labelSave = new JLabel("SAVE");
        labelSave.setFont(font.deriveFont(Font.BOLD, 43));
        labelSave.setForeground(colorBrown);
        labelSave.setHorizontalAlignment(JLabel.CENTER);
        labelSave.setBounds(485, 530, 230, 80);

        panel.add(labelName);
        panel.add(textName);
        panel.add(labelScore);
        panel.add(labelGetScore);
        panel.add(labelSave);
        panel.add(btnSave);

        // db 테이블에 값 insert 하기
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DBcon db = new DBcon();

                // db insert
                PreparedStatement ps= null;
                try {
                    ps = db.getCon().prepareStatement("insert into ham_score(userName, userScore) " +
                            "values('"+textName.getText()+"', "+score+");");
                    // 넘겨온 score 값과 입력받은 name을 table에 insert
                    ps.executeUpdate();
                    db.getCon().close();
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {
                    new Rank();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (FontFormatException ex) {
                    ex.printStackTrace();
                }
                ost.stop();
                setVisible(false); // 창 안보이게 하기

            }
        });

        this.add(panel);
        this.setVisible(true);
        this.setResizable(false);
    }

    class MyPanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backGround, 0, 0,width, height, this);

        }
    }

    class DBuse {

    }

    public static void main(String[] args) throws SQLException, IOException, FontFormatException {
        new Input();
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
}

