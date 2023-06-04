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

    // 설명 이미지 배열
    ImageIcon[] Images = {
        new ImageIcon("src/img/how/HOW_BASIC.png"),
        new ImageIcon("src/img/how/HOW_ITEM.png"),
        new ImageIcon("src/img/how/HOW_MONSTER.png"),
    };

    // 이미지 배열 인덱스
    int count = 0;

    Image backGround;
    ImageIcon introIcon = new ImageIcon("src/img/how/HOW_START_BUTTON.png");
    ImageIcon nextIcon = new ImageIcon("src/img/how/HOW_RIGHT_BUTTON.png");
    ImageIcon preIcon = new ImageIcon("src/img/how/HOW_LEFT_BUTTON.png");

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
        backGround = Images[0].getImage();

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
        btnNext.setBounds(895, 260, 75, 90);
        btnNext.setBorderPainted(false);
        btnNext.setContentAreaFilled(false);
        btnNext.setFocusPainted(false);
        btnNext.setVisible(true);

        // 이전으로 넘기기 버튼
        JButton btnPre = new JButton(preIcon);
        btnPre.setBounds(230, 260, 75, 90);
        btnPre.setBorderPainted(false);
        btnPre.setContentAreaFilled(false);
        btnPre.setFocusPainted(false);
        btnPre.setVisible(false);

        JButton btnIntro = new JButton(introIcon);
        btnIntro.setBounds(945, 665, 198, 78);
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
        // 이전 페이지
        btnPre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 첫 페이지일 경우
                if(count == 0) {
                    btnPre.setVisible(false);
                    btnNext.setVisible(true);
                    System.out.println("current index : " + count);
                }
                else {
                    count--;
                    backGround = Images[count].getImage();
                    btnPre.setVisible(true);
                    btnNext.setVisible(true);
                    System.out.println("current index : " + count);
                    if(count == 0) {
                        btnPre.setVisible(false);
                        btnNext.setVisible(true);
                        System.out.println("first page!!!");
                    }
                }
            }
        });
        // 다음 페이지
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnPre.setVisible(true);
                // 마지막 페이지일 경우
                if(count == 2) {
                    System.out.println("last page!!!");
                    btnNext.setVisible(false);
                    btnPre.setVisible(true);
                    System.out.println("current index : " + count);
                }
                else {
                    count++;
                    backGround = Images[count].getImage();
                    btnNext.setVisible(true);
                    btnPre.setVisible(true);
                    System.out.println("current index : " + count);
                    if(count == 2) {
                        System.out.println("last page!!!");
                        btnNext.setVisible(false);
                        btnPre.setVisible(true);
                    }
                }

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
