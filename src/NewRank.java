import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewRank extends JFrame {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private JPanel panel;

    public NewRank() {
        // JFrame 타이틀 설정
        setTitle("HowPage");

        // 배경 이미지를 위한 JPanel 생성
        panel = new JPanel() {
            // paintComponent 메소드를 오버라이딩하여 배경 이미지를 설정
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("src/img/Rank.png");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // JPanel 크기 및 레이아웃 설정
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        panel.setLayout(new FlowLayout());

        JButton moveMain = new JButton();
        moveMain.setBounds(950, 660, 200, 50);
        moveMain.setHorizontalAlignment(JLabel.CENTER);
        moveMain.setBorderPainted(false);
        moveMain.setContentAreaFilled(false);
        moveMain.setFocusPainted(false);

        moveMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Intro();
                setVisible(false); // 창 안보이게 하기
            }
        });

        // JPanel을 JFrame에 추가
        add(moveMain);
        add(panel);

        // 윈도우 종료 버튼을 눌렀을 때 실행되는 코드를 처리하는 WindowAdapter 객체 생성
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

    public static void main(String[] args) {
        NewRank frame = new NewRank();
    }
}
