import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class Rank extends JFrame {
    int width, height;
    Toolkit tk = Toolkit.getDefaultToolkit();

    JLabel[] nameArr = new JLabel[5];
    JLabel[] scoreArr = new JLabel[5];
    Font font;
    Color colorBrown = new Color(0x312500);

    Clip ost;

    // 사용 이미지 불러오기
    Image backGround = new ImageIcon("src/img/rankbackground.png").getImage();
    ImageIcon introIcon = new ImageIcon("src/img/back_intro.png");

    public void init() throws IOException, FontFormatException { // 컴포넌트 세팅
        width = 1200;
        height = 800;

    }

    public Rank() throws SQLException, IOException, FontFormatException {
        super("rank"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();

        ost = Sound("src/bgm/intro.wav", true);

        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);

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
        JButton btnIntro = new JButton(introIcon);
        btnIntro.setBounds(40, 655, 90, 90);
        btnIntro.setBorderPainted(false);
        btnIntro.setContentAreaFilled(false);
        btnIntro.setFocusPainted(false);

        JLabel nameLabel = new JLabel("NAME");
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setFont(font.deriveFont(Font.BOLD, 43));
        nameLabel.setForeground(colorBrown);
        nameLabel.setBounds(315, 170, 150, 65);

        JLabel scoreLabel = new JLabel("SCORE");
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setFont(font.deriveFont(Font.BOLD, 43));
        scoreLabel.setForeground(colorBrown);
        scoreLabel.setBounds(735, 170, 150, 65);

        panel.add(nameLabel);
        panel.add(scoreLabel);
        panel.add(btnIntro);

        // 화면 전환용 리스너
        btnIntro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Intro();
                ost.stop();
                setVisible(false); // 창 안보이게 하기
            }
        });

        // db table에서 select하여 상위 5개 순위 보여주기
        DBcon db = new DBcon();

        // db select
        Statement st = db.getCon().createStatement();
        ResultSet resultSet = st.executeQuery("SELECT * FROM " +
                "( SELECT * FROM ham_score ORDER BY userScore DESC )A " +
                "LIMIT 5");
        // 서브 쿼리를 활용하여 데이터 정렬
        // limit을 활용하여 불러오는 값을 5개로 제한

        // 불러온 값을 next하여 각 jlabel 배열에 저장 후 add 하여 화면에 출력
        int i=0;
        while(resultSet.next()){
            String name = resultSet.getString("userName");
            int score = resultSet.getInt("userScore");

            nameArr[i] = new JLabel(name);
            nameArr[i].setFont(font.deriveFont(Font.BOLD, 40));
            nameArr[i].setForeground(colorBrown);
            nameArr[i].setBounds(315, 270+70*i, 150, 60);
            nameArr[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(nameArr[i]);

            scoreArr[i] = new JLabel(Integer.toString(score));
            scoreArr[i].setFont(font.deriveFont(Font.BOLD, 40));
            scoreArr[i].setForeground(colorBrown);
            scoreArr[i].setBounds(735, 270+70*i, 150, 60);
            scoreArr[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(scoreArr[i]);

            System.out.println(name+" "+score);
            i++;
        }

        db.getCon().close();
        st.close();
        resultSet.close();

        this.setSize(width, height);
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

    public static void main(String[] args) throws SQLException, IOException, FontFormatException {
        new Rank();
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
