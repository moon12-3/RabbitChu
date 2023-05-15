import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SelectStage extends JFrame {
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private JPanel panel;

    public SelectStage() {
        // JFrame 타이틀 설정
        setTitle("SelectStage");


        panel = new JPanel() {
            // paintComponent 메소드를 오버라이딩하여 배경 이미지를 설정
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

// JPanel 크기 및 레이아웃 설정
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        panel.setLayout(null);



        JPanel level1frame = new JPanel(null);
        level1frame.setBounds(200, 200, 800, 150);
        level1frame.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JButton level1 = new JButton("LV.1");
        level1.setBounds(0, 0, 150, 150);
        level1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        level1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // level1 버튼을 눌렀을 때 이동
                dispose();
            }
        });

        JLabel level1Text = new JLabel();
        level1Text.setBounds(0, 0, 800, 150);
        level1Text.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JPanel level2frame = new JPanel(null);
        level2frame.setBounds(200, 400, 800, 150);
        level2frame.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JButton level2 = new JButton("LV.2");
        level2.setBounds(0, 0, 150, 150);
        level2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        level2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // level2 버튼을 눌렀을 때 이동
                dispose();
            }
        });

        JLabel level2Text = new JLabel();
        level2Text.setBounds(0, 0, 800, 150);
        level2Text.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));



        level1frame.add(level1Text);
        level1frame.add(level1);
        add(level1frame);

        level2frame.add(level2Text);
        level2frame.add(level2);
        add(level2frame);

        add(panel);

        JButton moveMain = new JButton("main");
        moveMain.setBounds(700, 600, 100, 100);
        panel.add(moveMain); // panel에 버튼 추가


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
        SelectStage frame = new SelectStage();
    }
}
