import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.*;

public class Main {
    public static void main(String args[]) {
        //new Frame_make();
        new Intro();
    }
}

class Frame_make extends JFrame implements KeyListener, Runnable{
    int width;
    int height;
    int cnt;
    int eCnt;
    int iCnt = 0;
    int hpCnt = 0;  // 데미지 입었을 때 깜빡거리는 용
    int hppCnt = 0;
    int bCnt = 0;
    int fire_speed;
    int control;

    // 게임 제어를 위한 변수
    int status; // 게임 상태

    // 게임용 변수
    public static int gameScore;
    int hp; // 남은 목숨
    int gameCnt; // 게임 흐름 컨트롤
    int deathTimer = 0;
    int level;  // 게임 레벨

    int x, y; // 캐릭터 위치
    int speed;  // 이동 속도
    int playerStatus = 0;   // 플레이어 캐릭터의 상태(0 : 보통 1: 데미지 2: 힐)
    // int shield; 쉴드

    int[] cx = {0, 0, 0};
    int bx = -4; // 배경 스크롤 변수
    int bx2 = 2275;
    int buDamage;   // 총알의 데미지

    //키보드 입력 처리를 위한 변수
    boolean KeyUp = false;
    boolean KeyDown = false;
    boolean KeyLeft = false;
    boolean KeyRight = false;
    boolean KeySpace = false;

    boolean isDamaged = false;
    boolean isHealed = false;
    boolean isBossDamaged = false;
    boolean boss = false;   // 보스관련 추가

    Thread th;  // 스레드 객체

    Toolkit tk = Toolkit.getDefaultToolkit();   // 이미지 불러오는 툴킷
    Image[] player;
    Image[] item_img;   // 아이템 이미지
    Image bullet;
    Image bullet2;  // 적총알
    Image enemy;
    Image boss_img;
    Image backGround1;
    Image[] Cloud_img; // 구름
    Image[] Explo_img;  // 폭발이펙트용
    Image heart;    // 남은 목숨 표시용
    ArrayList bulletList = new ArrayList();
    ArrayList enemyList = new ArrayList();
    ArrayList itemList = new ArrayList();
    ArrayList eBulletList = new ArrayList();
    ArrayList explosionList = new ArrayList();
    Image buffImage;    // 더블 버퍼링용
    Graphics buffg;     // 더블 버퍼링용 2

    Bullet bu;
    Enemy en;
    Item it;
    EnemyBullet eb;
    Explosion ex;
    BossEnemy be;

    Clip ost;

    Frame_make() {
        super("햄모험");
        //setIconImage(makeImage("./"));
        init();
        setSize(width, height);
        start();
        Dimension screen = tk.getScreenSize();
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);
        setVisible(true);
        setResizable(false);
    }

    public void init() { // 컴포넌트 세팅
        x = 100; //캐릭터의 최초 좌표.
        y = 100;
        hp = 100;    // 초기 캐릭터 생명 (하트 3개)
        width = 1200;
        height = 800;
        speed = 10;
        gameCnt = 2000;    // 보스전 테스트 시 2000으로 설정하면 됨
        buDamage = 5;
        playerStatus = 0;

        level = 2;  // 레벨(스테이지 선택 시 기본 값 조정)

        ost = Sound("src/bgm/OST.wav", true);// 배경 음악

        backGround1 = new ImageIcon("src/img/background1.png").getImage();
//        backGround1 = backGround1.getScaledInstance(2275, 800, Image.SCALE_SMOOTH);
        player = new Image[3];
        Image p = new ImageIcon("src/img/player.png").getImage();
        player[0] = p.getScaledInstance(85, 143, Image.SCALE_SMOOTH);
        p = new ImageIcon("src/img/playerDamaged.png").getImage();
        player[1] = p.getScaledInstance(85, 143, Image.SCALE_SMOOTH);
        p = new ImageIcon("src/img/player1.png").getImage();
        player[2] = p.getScaledInstance(85, 143, Image.SCALE_SMOOTH);
        bullet = new ImageIcon("src/img/bullet.png").getImage();
        bullet = bullet.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
        bullet2 = new ImageIcon("src/img/ebullet1.png").getImage();
        bullet2 = bullet2.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        enemy = new ImageIcon("src/img/enemy1.png").getImage();
        boss_img = new ImageIcon("src/img/boss.png").getImage();
        heart = new ImageIcon("src/img/heart.png").getImage();//
        heart = heart.getScaledInstance(71, 65, Image.SCALE_SMOOTH);

        Cloud_img = new Image[3];
        for(int i = 0; i < Cloud_img.length; i++) {
            Cloud_img[i] = new ImageIcon("src/img/cloud/cloud"+i+".png").getImage();
        }

        item_img = new Image[2];
        for(int i = 0; i < item_img.length; i++) {
            Image img = new ImageIcon("src/img/item/item_"+i+".png").getImage();
            item_img[i] = img.getScaledInstance(81-11*i, 76-6*i, Image.SCALE_SMOOTH);
        }

        Explo_img = new Image[9];
        for(int i = 0; i < Explo_img.length; i++) {
            Image img = new ImageIcon("src/img/explosion/explosion_00"+(i+1)+".png").getImage();
            Explo_img[i] = img.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        }

        gameScore = 0;  // 게임 점수
        fire_speed = 5; //총알 속도
    }

    public void start() { // 시작처리명령
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(this);    //키보드 이벤트 실행
        th = new Thread(this);  // 스레드 생성
        th.start();
    }

    @Override
    public void run() {
        final int saveScore;
        try{ // 예외옵션 설정으로 에러 방지

            while(true){ // while 문으로 무한 루프 시키기

                KeyProcess(); // 키보드 입력처리를 하여 x,y 갱신
                repaint(); // 갱신된 x,y값으로 이미지 새로 그리기
                Thread.sleep(20); // 20 milli sec 로 스레드 돌리기
                cnt++;
                iCnt++;
                eCnt++;
                gameCnt++;
                if(boss) {
                    // 보스가 생성되어 있는 경우
                    if(level==1) {  // 1챕터
                        if(800<gameCnt&&gameCnt<1000) {
                            if(gameCnt%60==0) {
                                enemyList.add(new Enemy(width+25, (int)(Math.random()*621)+30, 0));
                            }
                        }
                        else if(1600<gameCnt&&gameCnt<2200) {   // 보스전 후반부
                            if(gameCnt%50==0) {
                                enemyList.add(new Enemy(width+25, (int)(Math.random()*621)+30, 0));
                            }
                        }
                    }
                    else if(level==2) { // 2챕터
                        if(800<gameCnt&&gameCnt<1000) {
                            if(gameCnt%50==0) {
                                enemyList.add(new Enemy(width+25, (int)(Math.random()*621)+30, 0));
                            }
                        }
                        else if(1300<gameCnt&&gameCnt<1800) {   // 보스전 중반부
                            if(gameCnt%40==0) {
                                enemyList.add(new Enemy(width+25, (int)(Math.random()*621)+30, 0));
                            }
                        }
                        else if(2000<gameCnt&&gameCnt<2200) {   // 보스전 후반부
                            if(gameCnt%40==0) {
                                enemyList.add(new Enemy(width+25, (int)(Math.random()*621)+30, 1));
                            }
                        }
                    }
                    if(gameCnt>2210){   // 타임아웃
                        boss = false;
                        gameCnt=0;
                        ost.stop();
                        new GameOver();
                        setVisible(false);
                        System.out.println("보스 타임아웃");
                    }
                }
                else {
                    if(gameCnt<500) control = 1;
                    else if(gameCnt<1000) control = 2;
                    else if(gameCnt<1300) control = 0;
                    else if(gameCnt<1700) control = 1;
                    else if(gameCnt<2000) control = 2;
                    else if(gameCnt<2400) control = 3;
                    else{
                        System.out.println("보스 등장");
                        boss = true;
                        be = new BossEnemy(level, width, 250);
                        gameCnt = 0;
                    }
                }
                if(isDamaged) {
                    hpCnt++;
                    playerStatus = 1;
                }
                if(hpCnt == 50) {
                    hpCnt = 0;
                    playerStatus = 0;
                    isDamaged=false;
                }
                if(isHealed) {
                    hppCnt++;
                    playerStatus = 1;
                }
                if(hppCnt == 50) {
                    hppCnt = 0;
                    isHealed=false;
                    playerStatus = 0;
                }
                if(isBossDamaged) {
                    bCnt++;
                    boss_img = new ImageIcon("src/img/bossDamaged.png").getImage();
                }
                if(bCnt==30) {
                    boss_img = new ImageIcon("src/img/boss.png").getImage();
                    bCnt = 0;
                    isBossDamaged = false;
                }
                itemProcess();
                BulletProcess();
                ExplosionProcess();
                EnemyProcess();

                bx-=1;
                bx2-=1;

                // 배경 이어지게 하기
                if(bx < -(backGround1.getWidth(null))) {
                    bx = backGround1.getWidth(null);
                }
                if(bx2 < -(backGround1.getWidth(null))) {
                    bx2 = backGround1.getWidth(null);
                }

                if(hp<=0){
                    break;
                }
//                if(boss==false && level>1){
//                    deathTimer++;
//                }
//                if(deathTimer>=30) {
//                    break;
//                }

            }
            if(hp<=0){
                ost.stop();
                new GameOver();
                setVisible(false);
            }
            if(boss==false && level>1){
                ost.stop();
                GameClear gameClear = new GameClear();
                // 게임 점수 전달
                gameClear.setClearscore(gameScore);
                setVisible(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    int getAngle(int sx, int sy, int dx, int dy){
        int vx=dx-sx;
        int vy=dy-sy;
        double rad=Math.atan2(vx,vy);
        int degree=(int)((rad*180)/Math.PI);
        return (degree+180);
    }

    public void move(int shootType, Enemy en) { // 총알 쏘는 타입
        switch(shootType) {
            case 0 :
                break;
            case 1 :    // 플레이어를 향해 3발 점사
                if(eCnt%100==0 || eCnt%106==0 || eCnt%112==0) {
                    bu = new Bullet(en.x, en.y + 25, 90,15, 1);
                    bulletList.add(bu);
                }
                break;
            case 2 :    // 짧은 간격으로 플레이어 근처를 향해 한 발씩 발사
                if(eCnt%90==0||eCnt%120==0||eCnt%150==0) {
                    bu = new Bullet(en.x, en.y, (getAngle(en.x, en.y, x, y) + (int) (Math.random() * 40) - 20) % 360, 20, 1);
                    bulletList.add(bu);
                }
                break;
            case 3 :    // 3갈래탄
                if(eCnt%80==0) {
                    bu = new Bullet(en.x, en.y, getAngle(en.x, en.y, x, y),20,1);
                    bulletList.add(bu);
                    bu = new Bullet(en.x, en.y, (getAngle(en.x, en.y, x, y)-20)%180,20,1);
                    bulletList.add(bu);
                    bu = new Bullet(en.x, en.y, (getAngle(en.x, en.y, x, y)+20)%180,20,1);
                    bulletList.add(bu);
                }
                break;
            case 4 :    // 보스 전용
                if(eCnt%140==0||eCnt%145==0||eCnt%150==0||eCnt%155==0) {
                    for(int i = 0; i < 10; i ++) {
                        bu = new Bullet(en.x+140, en.y + 100, (30*i+(cnt%36)*10)%360,13, 1);
                        bulletList.add(bu);
                    }
                }
                break;
        }
    }


    public void EnemyProcess() {
        for(int i = 0; i < enemyList.size(); i++) {
            en = (Enemy) enemyList.get(i);
            en.move();
            if(en.x < -200) enemyList.remove(i);
            move(en.shootType, en);
            if(Crash(x, y, en.x, en.y, player[0], enemy, 2)) {  // 플레이어와 적 충돌
                isDamaged = true;
                if(hpCnt == 1) {
                    hp--;
                    bullet = new ImageIcon("src/img/bullet.png").getImage();
                    bullet = bullet.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
                    buDamage = 5;
                }
            }
        }
        if(boss) {
            be.move();
            move(be.shootType, be);
            if(Crash(x, y, be.x, be.y, player[0], boss_img, 2)) {   // 플레이어와 보스 충돌
                isDamaged = true;
                if(hpCnt == 1) {
                    hp--;
                    bullet = new ImageIcon("src/img/bullet.png").getImage();
                    bullet = bullet.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
                    buDamage = 5;
                }
            }
        }
        int enemys = 80;
        switch (control) {
            case 1 : enemys = 70;
                break;
            case 2 : enemys = 60;
                break;
            case 3 : enemys = 50;
                break;
        }
        if((eCnt % enemys) == 0 && !boss) {
            enemyList.add(new Enemy(width+25, (int)(Math.random()*621)+30, (int)(Math.random()*(control+1))));

        }
    }

    public void itemProcess() {
        for(int i = 0; i < itemList.size(); i++) {
            it = (Item)itemList.get(i);
            it.move();

            if(it.y > height+25) itemList.remove(i);

            if(Crash(x, y, it.x, it.y-25, item_img[0], player[0],2)) { // 플레이어와 아이템 충돌
                if(it.type%2==0) {  // 독버섯
                    isDamaged = true;
                    hp--;
                    bullet = new ImageIcon("src/img/bullet.png").getImage();
                    bullet = bullet.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
                    buDamage = 5;
                    itemList.remove(i);
                    gameScore -= 10;
                }
                else {  // 딸기
                    isHealed = true;
                    if(hp<16)
                        hp++;
                    bullet = new ImageIcon("src/img/bullet2.png").getImage();
                    bullet = bullet.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                    buDamage = 10;
                    itemList.remove(i);
                    gameScore+=10;
                }
            }
        }
        if((iCnt % 100) == 0) { // 100마다 아이템 생성
            itemList.add(new Item((int)(Math.random()*800)+200,-130));
        }
    }

    public void BulletProcess() {
        if(KeySpace) {
            if(cnt%fire_speed==0) {
                Sound("src/bgm/bullet.wav", false);
                //gameCnt+=3;
                bu = new Bullet(x+50, y+50, 0, 25, 0);
                bulletList.add(bu);
            }
        }
        else cnt = 14;
        for(int i = 0; i < bulletList.size(); i++) {
            bu = (Bullet)bulletList.get(i);
            bu.move();
            if(bu.x > width - 20 || bu.x < 0 || bu.y < 0 || bu.y > height) {    // 총알이 화면 밖으로 나가면 삭제
                bulletList.remove(i);
            }

            if(Crash(x, y, bu.x, bu.y, player[0], bullet2, 1) && bu.who == 1) { // 적의 총알이 플레이어와 닿은 경우
                isDamaged = true;
                bulletList.remove(i);
                hp--;
                bullet = new ImageIcon("src/img/bullet.png").getImage();
                bullet = bullet.getScaledInstance(34, 34, Image.SCALE_SMOOTH);
                buDamage = 5;

            }
            if(boss) {
                if(Crash(bu.x, bu.y, be.x, be.y, bullet, boss_img, 1) && bu.who == 0) {    // 보스와 플레이어의 총알 충돌
                    bulletList.remove(i);
                    isBossDamaged = true;
                    be.hp -= buDamage;
                }
                if( be.hp <= 0) {   // 보스의 피가 다 달았을 경우 삭제
                    boss = false;
                    gameScore+=200;
                    ex = new Explosion(be.x+200 + boss_img.getWidth(null)/2, be.y-50 + boss_img.getHeight(null)/2, 0);
                    explosionList.add(ex);
                    ex = new Explosion(be.x + boss_img.getWidth(null)/2, be.y + boss_img.getHeight(null)/2, 0);
                    explosionList.add(ex);
                    ex = new Explosion((be.x+100) + boss_img.getWidth(null)/2, be.y/2 + boss_img.getHeight(null)/2, 0);
                    explosionList.add(ex);
                    level++;    // 레벨 클리어 여부 저장
                    System.out.println(level);
                }
            }

            for(int j = 0; j < enemyList.size(); j++) {
                en = (Enemy) enemyList.get(j);
                if(Crash(bu.x, bu.y, en.x, en.y, bullet, enemy, 1) && bu.who == 0) {    // 적과 플레이어의 총알 충돌
                    bulletList.remove(i);
                    ((Enemy) enemyList.get(j)).hp -= buDamage;
                }

                if( ((Enemy) enemyList.get(j)).hp <= 0) {   // 적의 피가 다 달았을 경우 삭제
                    Sound("src/bgm/explo.wav", false);
                    enemyList.remove(j);
                    gameScore+=10;
                    ex = new Explosion(en.x + enemy.getWidth(null)/2, en.y + enemy.getHeight(null)/2, 0);
                    explosionList.add(ex);
                }
            }
        }

    }

    public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2, int c){
        boolean check = false;
        if ( Math.abs( ( x1 + img1.getWidth(null) / 2 )
                - ( x2 + img2.getWidth(null) / c ))
                < ( img2.getWidth(null) / 2 + img1.getWidth(null) / 2 )
                && Math.abs( ( y1 + img1.getHeight(null) / 2 )
                - ( y2 + img2.getHeight(null) / 2 ))
                < ( img2.getHeight(null)/2 + img1.getHeight(null)/2 ) ) //이미지 넓이, 높이값을 바로 받아 계산
            check = true;//위 값이 true면 check에 true를 전달

        return check;
    }

    public void ExplosionProcess() {
        for(int i= 0; i < explosionList.size(); i++) {
            ex = (Explosion)explosionList.get(i);
            ex.effect();
        }
    }

    @Override
    public void paint(Graphics g) {
        buffImage = createImage(width, height); //화면크기와 동일
        buffg = buffImage.getGraphics();    // 버퍼의 그래픽 객체를 얻기
        update(g);
    }

    public void update(Graphics g) {
        Draw_Background(); //그려진 배경 가져오기

        Draw_Item();

        Draw_Player();

        Draw_Bullet(); // 그려진 총알 가져오기
        Draw_Enemy();   // 그려진 적 가져오기

        Draw_Explosion();
        Draw_StatusText();

        g.drawImage(buffImage, 0, 0, this); // 화면에 버퍼에 그린 그림을 가져와 그리기
    }

    public void Draw_StatusText() {
        buffg.setFont(new Font("Default", Font.BOLD, 20));
        buffg.drawString("SCORE : " + gameScore, 50, 70);
        buffg.drawString("HP " + hp, 50, 90);
        buffg.drawString("Enemy Count : " + enemyList.size(), 50, 110);
        for(int i = 0; i < hp; i++) {
            buffg.drawImage(heart, (width-121)-i*75, 50, this);
        }
    }

    public void Draw_Enemy() {
        for(int i = 0; i < enemyList.size(); ++i) {
            en = (Enemy)enemyList.get(i);
            buffg.drawImage(enemy, en.x, en.y, this);
        }
        if(boss) {
            buffg.drawImage(boss_img, be.x, be.y, this);
        }
    }

    public void Draw_Item() {
        for(int i = 0; i< itemList.size(); i++) {
            it = (Item)itemList.get(i);
            buffg.drawImage(item_img[it.type%2], it.x, it.y, this);
        }
    }

    public void drawExplo(int idx, Explosion ex) {
        buffg.drawImage( Explo_img[idx], ex.x -
                Explo_img[idx].getWidth(null) / 2, ex.y -
                Explo_img[idx].getHeight(null) / 2, this);
    }

    public void Draw_Explosion() {
        for(int i = 0; i < explosionList.size(); i++) {
            ex=(Explosion)explosionList.get(i);
            if(ex.damage == 0) {
                if(ex.ex_cnt < 5) {
                    drawExplo(0, ex);
                }else if ( ex.ex_cnt < 10 ) {
                    drawExplo(1, ex);
                }else if ( ex.ex_cnt < 15 ) {
                    drawExplo(2, ex);
                }else if ( ex.ex_cnt < 20 ) {
                    drawExplo(3, ex);
                }else if ( ex.ex_cnt < 25 ) {
                    drawExplo(4, ex);
                }else if ( ex.ex_cnt < 30 ) {
                    drawExplo(5, ex);
                }else if ( ex.ex_cnt < 35 ) {
                    drawExplo(6, ex);
                }else if ( ex.ex_cnt < 40 ) {
                    drawExplo(7, ex);
                }else if ( ex.ex_cnt < 45 ) {
                    drawExplo(8, ex);
                } else if( ex.ex_cnt > 50 ) {
                    explosionList.remove(i);
                    ex.ex_cnt = 0;
                }
            }else{
                if ( ex.ex_cnt < 7  ) {
                    buffg.drawImage(Explo_img[0], ex.x + 120,
                            ex.y + 15, this);
                }else if ( ex.ex_cnt < 14 ) {
                    buffg.drawImage(Explo_img[1], ex.x + 60,
                            ex.y + 5, this);
                }else if ( ex.ex_cnt < 21 ) {
                    buffg.drawImage(Explo_img[0], ex.x + 5,
                            ex.y + 10, this);
                }else if( ex.ex_cnt > 21 ) {
                    explosionList.remove(i);
                    ex.ex_cnt = 0;
                }
            }
        }
    }


    public void Draw_Bullet() {

        for (int i = 0; i < bulletList.size(); i++) {

            bu = (Bullet)bulletList.get(i);
            if(bu.who == 0)buffg.drawImage(bullet, bu.x, bu.y, this);
            if(bu.who == 1) buffg.drawImage(bullet2, bu.x, bu.y, this);
        }
    }

    public void Draw_Background(){ //배경 이미지 그리는 부분
        buffg.drawImage(backGround1, bx, 0, this);
        buffg.drawImage(backGround1, bx2, 0, this);

        for(int i = 0; i < cx.length; i++) {
            if(cx[i] < 1400) {
                cx[i] += 5 + i * 3;
            }else {cx[i] = 0;}
            buffg.drawImage(Cloud_img[i], width - cx[i], 50+i*200, this);
        }
    }

    public void Draw_Player() {
        switch(playerStatus) {
            case 0 :    // 평상시
                if((iCnt/5%2)==0) {
                    buffg.drawImage(player[0], x, y, this);
                }
                else {
                    buffg.drawImage(player[2], x, y, this);
                }
                break;
            case 1 : // 충돌
                if((hpCnt/5%2)==0) {
                    buffg.drawImage(player[0], x, y, this);
                }
                else {
                    buffg.drawImage(player[1], x, y, this);
                }
                // buffg.drawImage(player[1], x, y, this);

                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP :
            case KeyEvent.VK_W: KeyUp = true; break;
            case KeyEvent.VK_DOWN :
            case KeyEvent.VK_S : KeyDown = true; break;
            case KeyEvent.VK_LEFT :
            case KeyEvent.VK_A : KeyLeft = true; break;
            case KeyEvent.VK_RIGHT :
            case KeyEvent.VK_D : KeyRight = true; break;
            case KeyEvent.VK_SPACE: KeySpace = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP :
            case KeyEvent.VK_W: KeyUp = false; break;
            case KeyEvent.VK_DOWN :
            case KeyEvent.VK_S : KeyDown = false; break;
            case KeyEvent.VK_LEFT :
            case KeyEvent.VK_A : KeyLeft = false; break;
            case KeyEvent.VK_RIGHT :
            case KeyEvent.VK_D : KeyRight = false; break;
            case KeyEvent.VK_SPACE: KeySpace = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void KeyProcess(){
        if(KeyUp == true && y>30) {
            y -= speed;
        }
        if(KeyDown == true && y<height-120) {
            y += speed;
        }
        if(KeyLeft == true && x>10) {
            x -= speed;
        }
        if(KeyRight == true && x<width-110) {
            x += speed;
        }
    }

    public Clip Sound(String file, boolean Loop){ //사운드 재생용 메소드
        Clip clip = null;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
            clip = AudioSystem.getClip();
            clip.open(ais);

            clip.start();
            if (Loop) clip.loop(-1);   // 계속 재생할 것인지

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }
}