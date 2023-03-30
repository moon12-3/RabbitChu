import javax.swing.*;
import java.awt.*;


public class EnemyBullet {
    Image image = new ImageIcon("src/img/bullet1.png").getImage();

    int x, y;
    int width = image.getWidth(null);
    int height = image.getHeight(null);
    int attack = 5;

    public EnemyBullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void fire() {
        this.x -= 9;
    }
}
