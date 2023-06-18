import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class SelectStage extends JFrame {
    private boolean firstClear;
    private boolean secondClear;

    public void retrieveClearStatus() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT first_clear, second_clear FROM rabbit_table";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                firstClear = resultSet.getBoolean("first_clear");
                secondClear = resultSet.getBoolean("second_clear");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve clear status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/rabbitScoreDB"; // Replace with your MySQL database URL
        String username = "root"; // Replace with your MySQL username
        String password = "@summer0573"; // Replace with your MySQL password

        return DriverManager.getConnection(url, username, password);
    }

    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private JPanel panel;

    public SelectStage() {
        // JFrame 타이틀 설정
        setTitle("SelectStage");

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - FRAME_WIDTH / 2);
        int ypos = (int) (screen.getHeight() / 2 - FRAME_HEIGHT / 2);
        setLocation(xpos, ypos);

        // JFrame 크기 설정
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(!firstClear){
                    ImageIcon imageIcon = new ImageIcon("src/img/selectpage/select_page_background.png");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }else{
                    ImageIcon imageIcon = new ImageIcon("src/img/selectpage/stage_open.png");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        // JPanel 크기 및 레이아웃 설정
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        panel.setLayout(null);

        JButton level1 = new JButton();
        level1.setBounds(232, 227, 722, 135);
        level1.setOpaque(false);
        level1.setContentAreaFilled(false);

        level1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // level1 버튼을 눌렀을 때 이동
                new Frame_make();
                setVisible(false);
            }
        });

        JButton level2 = new JButton();
        level2.setBounds(232, 392, 722, 135);
        level2.setOpaque(false);
        level2.setContentAreaFilled(false);

        if (!firstClear) {
            level2.setVisible(false);
        }
        level2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // level2 버튼을 눌렀을 때 이동
                new Frame_make2();
                setVisible(false);
            }
        });

        ImageIcon originalImageIcon = new ImageIcon("src/img/main.png");  // Replace with your image file path
        Image originalImage = originalImageIcon.getImage();

        int buttonWidth = 170;
        int buttonHeight = 70;
        Image scaledImage = originalImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        JButton imageButton = new JButton(scaledImageIcon);
        imageButton.setBounds(950, 640, buttonWidth, buttonHeight);
        imageButton.setOpaque(false);
        imageButton.setBorderPainted(false);
        imageButton.setContentAreaFilled(false);

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.main(new String[]{});
                dispose();
            }
        });

        panel.add(imageButton);

        add(level1);
        add(level2);
        add(panel);

        // 윈도우 종료 버튼을 눌렀을 때 실행되는 코드를 처리하는 WindowAdapter 객체 생성
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        // JFrame을 화면에 표시
        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SelectStage();
            }
        });
    }
}
