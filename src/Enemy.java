public class Enemy {    // 적 위치 파악 및 이동
    int x;
    int y;
    int hp;
    int shootType;

    Enemy(int x, int y, int shootType) {
        this.x = x;
        this.y = y;
        this.shootType = shootType;
        hp = 20;
    }

    public void move() {
        x-= 5;
    }
}
