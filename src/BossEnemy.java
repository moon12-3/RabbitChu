import java.awt.*;

public class BossEnemy extends Enemy{
    int cnt;
    int mode;
    int a = -1;
    BossEnemy(int level, int x, int y) {
        super(x, y, 4);
        hp = 1500+300*level;
        mode = 4;
        cnt = 1;
    }

    public void move() {
        if (x > 600) {
            x -= 35;
        } else if (x < 0) {
            x += 35;
        } else {
            if (mode == 4) {
                x -= 5;
            } else {
                x += 10;
            }
        }

        cnt++;

        if (cnt % 300 == 0) {
            cnt = 1;
            a++;
            mode = a % 4;
        }

        switch (mode) {
            case 4:
                break;
            case 0: // 위로 이동
            case 1:
                if (y > 60) {
                    y -= 10; // 움직임 활발하게 변경
                }
                if (cnt % 30 == 0) { // 움직임 활발하게 변경
                    mode = 4;
                }
                break;
            case 2: // 아래로 이동
            case 3:
                if (y < 500) {
                    y += 10; // 움직임 활발하게 변경
                }
                if (cnt % 30 == 0) { // 움직임 활발하게 변경
                    mode = 4;
                }
                break;
        }
    }

}

