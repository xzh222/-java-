package wzq;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class game extends JFrame implements MouseListener,imformation {
    public static void main(String[] args) {
        game mj=new game();
        mj.myJFrame();
    }
    public static final Color Light_Brown = new Color(153,102,0);
    int x = 0, y = 0;//保存棋子坐标
    int jl = 0;//步数
    int[][] SaveGame = new int[16][16];//保存每个棋子
    int [] bsx = new int[260];//保存棋子坐标
    int [] bsy = new int[260];
    int qc = 1;//记录白棋=2，黑棋=1
    int qn = 0;//判断棋子是否重复
    boolean canplay = true;//判断游戏是否开始和结束
    String go = "黑子先行";
    //---------------------------------------------------------------------------------------------------------------------
    //窗体
    public void myJFrame() {

        setTitle("五子棋"); //标题
        setSize(cw, cy); //窗口大小
        setResizable(false); //窗口是否可以改变大小=否
        setDefaultCloseOperation(game.EXIT_ON_CLOSE);//窗口关闭方式为关闭窗口同时结束程序

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;//获取屏幕宽度
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;//获取屏幕高度


        this.setLocation((width - 800) / 2, (height - 600) / 2); //设置窗口默认位置以屏幕居中

        this.addMouseListener(this);

        this.setVisible(true); //窗口是否显示=是
    }


    //---------------------------------------------------------------------------------------------------------------------
    //覆写paint方法,绘制界面
    public void paint(Graphics g) {

        //双缓冲技术防止屏幕闪烁
        BufferedImage bi = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g2 = bi.createGraphics();

        //获取图片路径
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/wzq/1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(image, 70, 50, this);//显示图片

        g2.setColor(Color.BLACK);//设置画笔颜色
        g2.setFont(new Font("华文行楷", 10, 50));//设置字体
        g2.drawString("   五子棋", 530, 100);//绘制字符

        //棋盘
        g2.setColor(Light_Brown);//设置画笔颜色
        g2.fillRect(qx-20, qy-20, qw+40, qh+40);//绘制棋盘背景矩形

        //开始按钮
        g2.setColor(Color.WHITE);//设置画笔颜色
        g2.fillRect(bx, by, bw, bh);//绘制开始按钮
        g2.setFont(new Font("华文行楷", 10, 30));//设置字体
        g2.setColor(Color.black);//设置画笔颜色
        g2.drawString("开始", 615, 185);//绘制字符

        //悔棋按钮
        g2.setColor(Color.LIGHT_GRAY);//设置画笔颜色
        g2.fillRect(bx, by + 60, bw, bh);//绘制悔棋按钮
        g2.setFont(new Font("华文行楷", 10, 30));//设置字体
        g2.setColor(Color.WHITE);//设置画笔颜色
        g2.drawString("悔棋", 615, 245);//绘制字符

        //认输按钮
        g2.setColor(Color.GRAY);//设置画笔颜色
        g2.fillRect(bx, by + 120, bw, bh);//绘制认输按钮
        g2.setFont(new Font("华文行楷", 10, 30));//设置字体
        g2.setColor(Color.WHITE);//设置画笔颜色
        g2.drawString("认输", 615, 305);//绘制字符

        //游戏信息栏
        g2.setColor(Color.getHSBColor(30, (float) 0.10, (float) 0.90));//设置画笔颜色
        g2.fillRect(550, 350, 200, 150);//绘制游戏状态区域
        g2.setColor(Color.black);//设置画笔颜色
        g2.setFont(new Font("黑体", 10, 20));//设置字体
        g2.drawString("游戏信息", 610, 380);//绘制字符
        g2.drawString(go, 610, 410);//绘制字符



        g2.setColor(Color.BLACK);//设置画笔颜色

        //绘制棋盘格线
        for (int x = 0; x <= qw; x += 35) {
            g2.drawLine(qx, x + qy, qw + qx, x + qy);//绘制一条横线
            g2.drawLine(x + qx, qy, x + qx, qh + qy);//绘制一条竖线
        }

        //绘制标注点
        for (int i = 3; i <= 11; i += 4) {
            for (int y = 3; y <= 11; y += 4) {
                g2.fillOval(35 * i + qx - 3, 35 * y + qy - 3, 6, 6);//绘制实心圆
            }
        }


        //绘制棋子
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (SaveGame[i][j] == 1)//黑子
                {
                    int sx = i * 35 + qx;
                    int sy = j * 35 + qy;
                    g2.setColor(Color.BLACK);
                    g2.fillOval(sx - 13, sy - 13, 26, 26);//绘制实心圆
                }
                if (SaveGame[i][j] == 2)//白子
                {
                    int sx = i * 35 + qx;
                    int sy = j * 35 + qy;
                    g2.setColor(Color.WHITE);
                    g2.fillOval(sx - 13, sy - 13, 26, 26);//绘制实心圆
                    g2.setColor(Color.BLACK);
                    g2.drawOval(sx - 13, sy - 13, 26, 26);//绘制空心圆
                }
            }
        }
        g.drawImage(bi, 0, 0, this);


    }


    //---------------------------------------------------------------------------------------------------------------------
    //判断输赢
    private boolean WinLose() {
        boolean flag = false;//输赢
        int count = 1;//相连数
        int color = SaveGame[x][y];//记录棋子颜色
        //判断横向棋子是否相连

        int i = 1;//迭代数
        while (x+i>=0&&y>=0&&color == SaveGame[x + i][y]) {
            count++;
            i++;
        }
        i = 1;//迭代数
        while (x-i>=0&&y>=0&&color == SaveGame[x - i][y]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
        }

        //判断纵向棋子是否相连
        count = 1;
        i = 1;//迭代数
        while (x>=0&&y+i>=0&&color == SaveGame[x][y + i]) {

            count++;
            i++;
        }
        i = 1;//迭代数
        while (x>=0&&y-i>=0&&color == SaveGame[x][y - i]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
        }

        //判断斜向棋子是否相连（左上右下）
        count = 1;
        i = 1;//迭代数
        while (x-i>=0&&y-i>=0&&color == SaveGame[x - i][y - i]) {
            count++;
            i++;
        }
        i = 1;//迭代数
        while (x+i>=0&&y+i>=0&&color == SaveGame[x + i][y + i]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
        }

        //判断斜向棋子是否相连（左下右上）
        count = 1;
        i = 1;//迭代数
        while (x+i>=0&&y-i>=0&&color == SaveGame[x + i][y - i]) {
            count++;
            i++;
        }
        i = 1;//迭代数
        while (x-i>=0&&y+i>=0&&color == SaveGame[x - i][y + i]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
        }

        return flag;
    }


    //---------------------------------------------------------------------------------------------------------------------
    //初始化游戏
    public void Initialize() {
        //遍历并初始化数组
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                SaveGame[i][j] = 0;

            }
        }

        //黑子先行
        qc = 1;
        go = "轮到黑子";
    }


    //---------------------------------------------------------------------------------------------------------------------

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

        //获取鼠标点击位置
        x = e.getX();
        y = e.getY();


        //判断是否已开始游戏
        if (canplay == true) {
            //判断点击是否为棋盘内
            if (x > qx && x < qx + qw && y > qy && y < qy + qh) {
                //计算点击位置最近的点
                if ((x - qx) % 35 > 17) {
                    x = (x - qx) / 35 + 1;
                } else {
                    x = (x - qx) / 35;
                }
                if ((y - qy) % 35 > 17) {
                    y = (y - qy) / 35 + 1;
                } else {
                    y = (y - qy) / 35;
                }

                //判断当前位置有没有棋子
                if (SaveGame[x][y] == 0) {
                    SaveGame[x][y] = qc;
                    qn = 0;
                    jl++;
                    bsx[jl] = x;
                    bsy[jl] = y;

                } else {
                    qn = 1;
                }

                //切换棋子
                if (qn == 0) {
                    if (qc == 1) {
                        qc = 2;
                        go = "轮到白子";
                    } else {
                        qc = 1;
                        go = "轮到黑子";
                    }
                }

                this.repaint();//重新执行一次paint方法

                //弹出胜利对话框
                boolean wl = this.WinLose();
                if (wl) {
                    JOptionPane.showMessageDialog(this, "游戏结束，" + (SaveGame[x][y] == 1 ? "黑方赢了" : "白方赢了"));//弹出提示对话框
                    canplay = false;
                }


//            System.out.println(1);//测试
            } else {
//            System.out.println(0);//测试
            }
        }


        //实现开始按钮
        //判断是否点击开始按钮
        if (e.getX() > bx && e.getX() < bx + bw && e.getY() > by && e.getY() < by + bh) {
            //判断游戏是否开始
            if (canplay == false) {
                //如果游戏结束，则开始游戏
                canplay = true;
                JOptionPane.showMessageDialog(this, "游戏开始");
                //初始化游戏
                Initialize();

                this.repaint();//重新执行一次paint方法

            } else {
                //如果游戏进行中，则重新开始
                JOptionPane.showMessageDialog(this, "重新开始");
                //初始化游戏
                Initialize();

                this.repaint();//重新执行一次paint方法

            }
        }


        //实现悔棋按钮
        //判断是否点击悔棋按钮
        if (e.getX() > bx && e.getX() < bx + bw && e.getY() > by + 60 && e.getY() < by + 60 + bh) {
            //判断游戏是否开始
            if (canplay == true) {
                //遍历棋盘上是否有棋子
                int z = 0;
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        if (SaveGame[i][j] != 0) {
                            z++;
                        }
                    }
                }
                //判断是否有棋子
                if (z != 0) {
                    SaveGame[bsx[jl]][bsy[jl]] = 0;//实现悔棋
                    jl--;//步数后退
                    if(qc==1)
                        qc = 2;
                    else
                        qc = 1;
                    this.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "棋盘上已无棋子");
                }

            } else {
                JOptionPane.showMessageDialog(this, "请先开始游戏");
            }
        }


        //实现认输按钮
        //判断是否点击认输按钮
        if (e.getX() > bx && e.getX() < bx + bw && e.getY() > by + 120 && e.getY() < by + 120 + bh) {
            //判断游戏是否开始
            if (canplay == true) {
                //判断是谁认输
                if (qc == 1) {
                    JOptionPane.showMessageDialog(this, "黑方认输，白方获胜");
                    canplay = false;
                } else if (qc == 2) {
                    JOptionPane.showMessageDialog(this, "白方认输，黑方获胜");
                    canplay = false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "请先开始游戏");
            }
        }


    }



    public void mouseReleased(MouseEvent e) {

    }


    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
