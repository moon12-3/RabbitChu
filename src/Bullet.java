import java.awt.*;

public class Bullet {   // 총알 위치 파악 및 이동 위한 클래스
    int x;
    int y;
    int angle;  // 쏜 앵글
    int speed;

    int who;    // 누가 총을 쐈는지

    Bullet(int x, int y, int angle, int speed, int who) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.who = who;
        this.angle = angle;
    }

    public void move() {
        if(who==0){ // 플레이어라면
            x += Math.cos(Math.toRadians(angle)) * speed;
            y += Math.sin(Math.toRadians(angle)) * speed;
        }
        else{   // 몬스터라면
            x -= Math.sin(Math.toRadians(angle)) * speed;
            y -= Math.cos(Math.toRadians(angle)) * speed;
        }
    }
}
