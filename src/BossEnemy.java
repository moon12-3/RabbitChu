import java.awt.*;

public class BossEnemy extends Enemy {
    int cnt; // 일정 프레임마다 동작 변경
    int mode; // 보스 이동 모드 0 ~ 3이 값에 따라 이동 모드가 변경
    int a = -1; // 모드 변경하는 변수
    boolean movingForward; // 보스가 앞으로 이동하는가?
    int distanceMoved; // 보스가 이동한 거리
    int speed; // 보스 속도
    int restCounter; // 머무르는 시간 측정 카운터
    boolean isResting; // 만약 5초동안 쉬면 true로 바뀌는 값
    int moveDirection; // 보스 위아래 이동 방향, 1이면 위로, -1이면 아래로
    int moveCounter; // 앞으로 오는 횟수는 5번 정도로... 왜냐하면 너무 많이 움직여서 어려움

    BossEnemy(int level, int x, int y) {
        super(x, y, 4);
        hp = 1000 + 300 * level;
        mode = 4;
        cnt = 1;
        movingForward = true;
        distanceMoved = 0;
        speed = 7;
        restCounter = 0;
        isResting = false;
        moveDirection = 1;
        moveCounter = 0;
    }

    public void move() {
        if (movingForward) {
            if (x > 100) {
                x -= speed;
                distanceMoved += speed;
                speed += 1; // 속도를 점점 빠르게 증가시킴
            } else {
                movingForward = false;
                speed = 7; // 속도 초기화
                isResting = true;
                restCounter = 0;
            }
        } else {
            if (distanceMoved > 0) {
                x += speed;
                distanceMoved -= speed;
                speed += 1; // 속도를 점점 빠르게 증가시킴
            } else {
                movingForward = true;
                speed = 7; // 속도 초기화
                isResting = true;
                restCounter = 0;
                moveCounter++;
                if (moveCounter >= 5) { // 앞으로 오는 횟수를 5번으로 제한
                    moveDirection *= -1; // 위아래 이동 방향을 반대로 변경
                    moveCounter = 0;
                }
            }
        }

        cnt++;

        if (isResting) {
            restCounter++;
            if (restCounter >= 300) { // 300프레임 (약 5초) 동안 머무르도록 설정
                isResting = false;
            }
        } else {
            if (cnt % 120 == 0) { // 더 짧은 간격으로 이동 패턴을 변경
                cnt = 1;
                a++;
                mode = a % 4;
            }
        }

        switch (mode) {
            case 4:
                break;
            case 0: // 위로 이동
            case 1:
                if (y > 60) y -= 7;
                if (cnt % 240 == 0) mode = 4; // 더 짧은 간격으로 이동 패턴 변경
                break;
            case 2: // 아래로 이동
            case 3:
                if (y < 500) y += 7;
                if (cnt % 240 == 0) mode = 4; // 더 짧은 간격으로 이동 패턴 변경
                break;
        }

        if (moveDirection == 1) {
            y -= 3; // 위로 이동
        } else {
            y += 3; // 아래로 이동
        }
    }
}
