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
import java.io.InputStream;

public class GameOver extends JFrame{
    int width, height;
    Toolkit tk = Toolkit.getDefaultToolkit();

    Font font;
    Color colorBrown = new Color(0x312500);

    Image backGround = new ImageIcon("src/img/back_gameover.png").getImage();

    Clip ost;

    public void init() { // 컴포넌트 세팅
        width = 1200;
        height = 800;

    }

    public GameOver() throws IOException, FontFormatException {
        super("over"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();

        ost = Sound("src/bgm/gameover.wav", false);

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

        // 버튼 추가 및 설정
        JButton btnReturn = new JButton("다시하기");
        btnReturn.setBounds(250, 530, 230, 80);
        btnReturn.setFont(font.deriveFont(Font.BOLD, 40));
        btnReturn.setForeground(colorBrown);
        btnReturn.setHorizontalAlignment(JLabel.CENTER);
        btnReturn.setBorderPainted(false);
        btnReturn.setContentAreaFilled(false);
        btnReturn.setFocusPainted(false);

        JButton btnIntro = new JButton("메인으로");
        btnIntro.setBounds(710, 530, 230, 80);
        btnIntro.setFont(font.deriveFont(Font.BOLD, 40));
        btnIntro.setForeground(colorBrown);
        btnIntro.setHorizontalAlignment(JLabel.CENTER);
        btnIntro.setBorderPainted(false);
        btnIntro.setContentAreaFilled(false);
        btnIntro.setFocusPainted(false);

        panel.add(btnReturn);
        panel.add(btnIntro);

        // 화면 전환용 리스너
        btnReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Frame_make();
                ost.stop();
                setVisible(false); // 창 안보이게 하기
            }
        });

        btnIntro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Intro();
                ost.stop();
                setVisible(false); // 창 안보이게 하기
            }
        });

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

    public static void main(String[] args) throws IOException, FontFormatException {
        new GameOver();
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
