import java.awt.*;

public class BossEnemy extends Enemy {
    int cnt; // 움직임 타이밍
    int mode; // 보스 이동 방향
    int a = -1; // 앞뒤 이동 횟수
    boolean movingForward; // 보스 앞으로 이동중인가?
    int distanceMoved; // 보스가 앞, 뒤로 이동한 거리

    BossEnemy(int level, int x, int y) {
        super(x, y, 4);
        hp = 1000 + 300 * level;
        mode = 4;
        cnt = 1;
        movingForward = true;
        distanceMoved = 0;
    }

    public void move() {
        if (movingForward) { // true면 앞으로
            if (x > 300) {
                x -= 7;
                distanceMoved += 7;
            } else {
                movingForward = false;
            }
        } else { // false면 뒤로
            if (distanceMoved > 0) {
                x += 7;
                distanceMoved -= 7;
            } else {
                movingForward = true;
            }
        }

        cnt++; // cnt를 증가시키고 mode 변경해서 보스 움직임 변경

        if (cnt % 180 == 0) {
            cnt = 1;
            a++;
            mode = a % 4;
        }

        switch (mode) {
            case 4:
                break;
            case 0: // 위로 이동
            case 1:
                if (y > 60) y -= 5;
                if (cnt % 360 == 0) mode = 4;
                break;
            case 2: // 아래로 이동
            case 3:
                if (y < 500) y += 5;
                if (cnt % 360 == 0) mode = 4;
                break;
        }
    }
}
