import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class plat {
    public static void main(String[] args) {
        new Frame_make2();
    }
}

class Frame_make2 extends JFrame implements KeyListener, Runnable{
    int width;
    int height;
    int cnt;
    int fire_speed;

    int x, y; // 캐릭터의 좌표 변수
    int bx = 0; // 배경 스크롤 변수
    int bx2 = 2107;
    boolean KeyUp = false; //키보드 입력 처리를 위한 변수
    boolean KeyDown = false;
    boolean KeyLeft = false;
    boolean KeyRight = false;
    boolean KeySpace = false;

    Thread th;  // 스레드 생성

    Toolkit tk = Toolkit.getDefaultToolkit();   // 이미지 불러오는 툴킷
    Image player;
    Image bullet;
    Image enemy;
    Image backGround1;
    Image buffImage;    // 더블 버퍼링용
    Graphics buffg;     // 더블 버퍼링용 2

    Bullet bu;
    Enemy en;

    Frame_make2() {
        super("햄모험");
        init();
        setSize(width, height);
        start();
        Dimension screen = tk.getScreenSize();
//        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); // 현재 모니터 해상도 값
        int xpos = (int) (screen.getWidth() / 2 - width / 2);
        int ypos = (int) (screen.getHeight() / 2 - height / 2);
        setLocation(xpos, ypos);
        setVisible(true);
        setResizable(false);
    }

    public void init() { // 컴포넌트 세팅
        x = 100; //캐릭터의 최초 좌표.
        y = 100;
        width = 1200;
        height = 800;

        fire_speed = 15; //총알 속도
        backGround1 = tk.getImage("src/img/background2.jpg");
        player  = tk.getImage("src/img/player.png");
    }

    public void start() { // 시작처리명령
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(this);    //키보드 이벤트 실행
        th = new Thread(this);  // 스레드 생성
        th.start();
    }

    @Override
    public void run() {
        try{ // 예외옵션 설정으로 에러 방지

            while(true){ // while 문으로 무한 루프 시키기
                KeyProcess(); // 키보드 입력처리를 하여 x,y 갱신
                repaint(); // 갱신된 x,y값으로 이미지 새로 그리기
                Thread.sleep(20); // 20 milli sec 로 스레드 돌리기
                cnt++;

                bx--;
                bx2--;

                if(bx < -(backGround1.getWidth(null))) {
                    bx = backGround1.getWidth(null);
                }
                if(bx2 < -(backGround1.getWidth(null))) {
                    bx2 = backGround1.getWidth(null);
                }
                repaint();
                try{
                    Thread.sleep(3);
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void paint(Graphics g) {
        buffImage = createImage(width, height); //화면크기와 동일
        buffg = buffImage.getGraphics();    // 버퍼의 그래픽 객체를 얻기
        update(g);
//        g.clearRect(0, 0, width, height); // 0,0 에서 위에서 정한 해상도 크기만큼 화면을 지움
//        g.drawImage(player, x, y, this); // (x, y)에 player 이미지를 그려넣음

    }

    public void update(Graphics g) {
        Draw_Background(); //그려진 배경 가져오기

        Draw_Char(); // 실제로 그려진 그림 가져오기

        g.drawImage(buffImage, 0, 0, this); // 화면에 버퍼에 그린 그림을 가져와 그리기
    }


    public void Draw_Background(){ //배경 이미지 그리는 부분

        buffg.drawImage(backGround1, bx, 0, this);
        buffg.drawImage(backGround1, bx2, 0, this);

    }

    public void Draw_Char() {    // 실제로 그림들을 그릴 부분

        buffg.drawImage(player, x, y, this);
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
        if(KeyUp == true && y>-30) y -= 7;
        if(KeyDown == true && y<height-200) y += 7;
        if(KeyLeft == true && x>-68) x -= 7;
        if(KeyRight == true && x<width-210) x += 7;
    }
}
