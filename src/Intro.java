import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class Intro extends JFrame {
    int width, height;
    Toolkit tk = Toolkit.getDefaultToolkit();

    // 사용 이미지 불러오기
    Image backGround = new ImageIcon("src/img/background_intro.png").getImage();
    ImageIcon startIcon = new ImageIcon("src/img/start_btn.png");
    ImageIcon readyIcon = new ImageIcon("src/img/ready_btn.png");
    ImageIcon rankIcon = new ImageIcon("src/img/rank_btn.png");
    ImageIcon logoIcon = new ImageIcon("src/img/main_logo.png");
    Clip ost;

    public void init() { // 컴포넌트 세팅
        width = 1200;
        height = 800;
    }

    public Intro() {
        super("intro"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();
        ost = Sound("src/bgm/intro.wav", true);

        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);

        MyPanel panel = new MyPanel();
        panel.setLayout(null);

        // 버튼, 라벨 추가 및 설정
        JButton btnStart = new JButton(startIcon);
        btnStart.setBounds(85, 440, 250, 250);
        btnStart.setBorderPainted(false);
        btnStart.setContentAreaFilled(false);
        btnStart.setFocusPainted(false);

        JButton btnRank = new JButton(rankIcon);
        btnRank.setBounds(500, 450, 220, 220);
        btnRank.setBorderPainted(false);
        btnRank.setContentAreaFilled(false);
        btnRank.setFocusPainted(false);

        JButton btnReady = new JButton(readyIcon);
        btnReady.setBounds(845, 440, 250, 250);
        btnReady.setBorderPainted(false);
        btnReady.setContentAreaFilled(false);
        btnReady.setFocusPainted(false);

        JLabel labelLogo = new JLabel(logoIcon);
        labelLogo.setBounds(300, 50, 600, 400);

        panel.add(labelLogo);
        panel.add(btnStart);
        panel.add(btnRank);
        panel.add(btnReady);

        this.setSize(width, height);
        this.add(panel);
        this.setVisible(true);
        this.setResizable(false);

        // 화면 전환용 리스너
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Frame_make();
                ost.stop();
                setVisible(false); // 창 안보이게 하기
            }
        });

        btnRank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        btnReady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Ready(ost);
                setVisible(false); // 창 안보이게 하기
            }
        });
    }

    class MyPanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backGround, 0, 0,width, height, this);

        }
    }
    public static void main(String[] args) {
        new Intro();
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