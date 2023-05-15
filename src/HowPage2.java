import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HowPage2 {
    private static JFrame frame;
    private static ImageIcon[] imageIcons = {new ImageIcon("src/img/how/HOW_BASIC.png"), new ImageIcon("src/img/how/HOW_ITEM.png"), new ImageIcon("src/img/how/HOW_MONSTER.png")};
    private static int currentImageIndex = 0;

    public static void main(String[] args) {
        // JFrame 생성
        frame = new JFrame();
        frame.setTitle("HowPage");

        frame.addWindowListener(new MyWindowAdapter());

        // JFrame 크기 설정
        frame.setSize(1200, 800);

        // 배경 이미지를 위한 JPanel 생성
        JPanel panel = new JPanel() {
            // paintComponent 메소드를 오버라이딩하여 배경 이미지를 설정
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageIcons[currentImageIndex].getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        // JPanel 크기 및 레이아웃 설정
        panel.setPreferredSize(new Dimension(1200, 800));
        panel.setLayout(new FlowLayout());
        panel.setLayout(null);

        // 버튼 생성
        JButton startBtn = new JButton("");

        startBtn.setBounds(950, 650,150, 50);
        startBtn.setOpaque(false);
        startBtn.setContentAreaFilled(false);
        startBtn.setBorderPainted(false);

        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // loginPage로 이동
                LoginPage loginpPage = new LoginPage();
                loginpPage.setVisible(true);
                frame.dispose();
            }
        });


        JButton prevBtn = new JButton("");

        prevBtn.setBounds(250, 270, 50, 50);
        prevBtn.setOpaque(false);
        prevBtn.setContentAreaFilled(false);
        prevBtn.setBorderPainted(false);
        prevBtn.setPreferredSize(new Dimension(100, 50));
        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex--;
                if (currentImageIndex < 0) {
                    currentImageIndex = imageIcons.length - 1;
                }
                panel.repaint();
            }
        });


        // 다음 이미지로 변경하는 버튼 생성
        JButton nextBtn = new JButton("");
        nextBtn.setBounds(900, 270, 50, 50);
        nextBtn.setOpaque(false);
        nextBtn.setContentAreaFilled(false);
        nextBtn.setBorderPainted(false);
        nextBtn.setPreferredSize(new Dimension(100, 50));
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex++;
                if (currentImageIndex >= imageIcons.length) {
                    currentImageIndex = 0;
                }
                panel.repaint();
            }
        });

        // JPanel에 버튼 추가
        panel.add(startBtn);
        panel.add(prevBtn);
        panel.add(nextBtn);




        // JFrame에 JPanel 추가
        frame.getContentPane().add(panel);

        // JFrame 보이기
        frame.setVisible(true);
    }

    static class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            // 윈도우 종료 버튼을 눌렀을 때 실행되는 코드
            System.exit(0);
        }
    }
}
