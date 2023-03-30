import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Ready extends JFrame {
    int width, height;
    Toolkit tk = Toolkit.getDefaultToolkit();

    Font font;

    // 사용 이미지 불러오기
    ImageIcon backGround1 = new ImageIcon("src/img/back_ready1.png");
    ImageIcon backGround2 = new ImageIcon("src/img/back_ready2.png");
    Image backGround;
    ImageIcon introIcon = new ImageIcon("src/img/back_intro.png");
    ImageIcon nextIcon = new ImageIcon("src/img/btn_next.png");
    ImageIcon preIcon = new ImageIcon("src/img/btn_pre.png");

    public void init() { // 컴포넌트 세팅
        width = 1200;
        height = 800;
    }

    public Ready(Clip ost) {
        super("ready"); // 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        init();

        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);

        MyPanel panel = new MyPanel();
        panel.setLayout(null);
        backGround = backGround1.getImage();

        // 외부 폰트 사용하기
        try {
            InputStream inputStream = new BufferedInputStream(
                    new FileInputStream("src/font/dunggeunmo.ttf"));

            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // 버튼, 라벨 추가 및 설정
        JButton btnNext = new JButton(nextIcon);
        btnNext.setBounds(1025, 120, 50, 70);
        btnNext.setBorderPainted(false);
        btnNext.setContentAreaFilled(false);
        btnNext.setFocusPainted(false);

        JButton btnPre = new JButton(preIcon);
        btnPre.setBounds(125, 120, 50, 70);
        btnPre.setBorderPainted(false);
        btnPre.setContentAreaFilled(false);
        btnPre.setFocusPainted(false);
        btnPre.setVisible(false);

        JButton btnIntro = new JButton(introIcon);
        btnIntro.setBounds(40, 655, 90, 90);
        btnIntro.setBorderPainted(false);
        btnIntro.setContentAreaFilled(false);
        btnIntro.setFocusPainted(false);

        panel.add(btnIntro);
        panel.add(btnNext);
        panel.add(btnPre);

        this.setSize(width, height);
        this.add(panel);
        this.setVisible(true);
        this.setResizable(false);

        // 화면 전환용 리스너
        btnPre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backGround = backGround1.getImage();
                btnNext.setVisible(true);
                btnPre.setVisible(false);
            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backGround = backGround2.getImage();
                btnNext.setVisible(false);
                btnPre.setVisible(true);
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
    }


    class MyPanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backGround, 0, 0,width, height, this);
            repaint();

        }
    }
    public static void main(String[] args) {
        new Intro();
    }
}
