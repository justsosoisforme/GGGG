package com.hlju.Tetris;

import java.awt.Color;//用于绘制图形的类
import java.awt.Graphics;//用于绘制图形的类
import java.awt.Point;//表示一个坐标点
import java.awt.event.ActionEvent;//表示用户操作产生的事件
import java.awt.event.ActionListener;//用于监听ActionEvent事件
import java.awt.event.KeyEvent;//表示键盘的按键

import javax.swing.JOptionPane;//用于弹出对话框
import javax.swing.JPanel;//一个容器，用于组合和管理其他组件
import javax.swing.Timer;//定时器，用于执行一个任务

public class Tetris extends JPanel {

    private static final long serialVersionUID = -807909536278284335L;
   //表示方块的大小，值为10
    private static final int BlockSize = 10;
    //表示游戏区域的宽度，即每一行可以放多少个方块，值为16
    private static final int BlockWidth = 16;
    //表示游戏区域的高度，即游戏区域有多少行，值为26
    private static final int BlockHeigth = 26;
    //表示下落时间间隔，值为1000毫秒
    private static final int TimeDelay = 1000;
    //保存游戏制作者信息的字符串数组
    private static final String[] AuthorInfo = {
            "制作人：","Jss"

    };

    // 存放已经固定的方块
    //一个二维布尔数组，用于存放固定下来的方块的状态，即表示游戏区域中哪些位置已经有方块，哪些位置为空
    private boolean[][] BlockMap = new boolean[BlockHeigth][BlockWidth];

    // 分数，初始值为0
    private int Score = 0;

    //是否暂停，初始值为false
    private boolean IsPause = false;

    // 7种形状
    static boolean[][][] Shape = BlockV4.Shape;

    // 下落方块的位置,左上角坐标
    private Point NowBlockPos;

    // 当前方块矩阵，即当前正在下落的方块的状态
    private boolean[][] NowBlockMap;
    // 下一个方块矩阵
    private boolean[][] NextBlockMap;
    //下一个要出现的方块的状态值
    private int NextBlockState;
    //表示正在下落的方块的状态值
    private int NowBlockState;

    //计时器
    private Timer timer;
    //构造函数
    public Tetris() {
        this.Initial();//调用Initial函数初始化游戏
        timer = new Timer(Tetris.TimeDelay, this.TimerListener);
        timer.start();//使用timer对象来定时执行TimerListener方法
        this.addKeyListener(this.KeyListener);//使用addKeyListener()函数添加一个KeyListener来监听键盘事件
    }
    //SetMode方法用于设置游戏的模式
    //如果设置为"v6"模式，则将静态数组Shape设置为BlockV6.Shape，否则设置为BlockV4.Shape，
    // 并调用Initial函数来重新初始化游戏
    public void SetMode(String mode){
        if (mode.equals("v6")){
            Tetris.Shape = BlockV6.Shape;
        }
        else{
            Tetris.Shape = BlockV4.Shape;
        }
        this.Initial();
        this.repaint();
    }
    //getNextBlock方法用于获得下一个方块
    private void getNextBlock() {
        // 将已经生成好的下一次方块赋给当前方块
        this.NowBlockState = this.NextBlockState;
        this.NowBlockMap = this.NextBlockMap;
        // 再次生成下一次方块
        this.NextBlockState = this.CreateNewBlockState();
        this.NextBlockMap = this.getBlockMap(NextBlockState);
        // 计算方块位置
        this.NowBlockPos = this.CalNewBlockInitPos();
    }
    //IsTouch方法：用于判断下一个方块是否会与已经固定的方块或者游戏区域的边界相碰撞
    private boolean IsTouch(boolean[][] SrcNextBlockMap,Point SrcNextBlockPos) {
        //该方法接受两个参数：SrcNextBlockMap表示下一个方块的状态，SrcNextBlockPos表示下一个方块的位置
        for (int i = 0; i < SrcNextBlockMap.length;i ++){
            for (int j = 0;j < SrcNextBlockMap[i].length;j ++){
                //该方法通过两个for循环遍历下一个方块的状态数组，判断方块的每个小单位是否与周围的方块或边界相接触
                if (SrcNextBlockMap[i][j]){
                    if (SrcNextBlockPos.y + i >= Tetris.BlockHeigth || SrcNextBlockPos.x + j < 0 || SrcNextBlockPos.x + j >= Tetris.BlockWidth){
                        return true;//如果下一个方块会与游戏区域的边界相碰撞（即超过了游戏区域的边界），则返回true
                    }
                    else{
                        if (SrcNextBlockPos.y + i < 0){
                            continue;
                        }
                        else{
                            if (this.BlockMap[SrcNextBlockPos.y + i][SrcNextBlockPos.x + j]){
                                return true;//如果下一个方块会与已经固定的方块相碰撞，则返回true
                            }
                        }
                    }
                }
            }
        }
        return false;//如果下一个方块没有碰撞，则返回false
    }
    //FixBlock方法：用于将当前下落的方块固定在游戏区域中
    private boolean FixBlock(){
        for (int i = 0;i < this.NowBlockMap.length;i ++){
            for (int j = 0;j < this.NowBlockMap[i].length;j ++){
                //该方法通过两个for循环遍历当前下落方块的状态数组
                if (this.NowBlockMap[i][j])
                    if (this.NowBlockPos.y + i < 0)
                        return false;//如果方块当前位置在游戏区域外部，则返回false
                    else
                        //否则将方块状态信息写入到BlockMap数组中，这里BlockMap数组的下标为当前方块的左上角坐标+方块的偏移量
                        this.BlockMap[this.NowBlockPos.y + i][this.NowBlockPos.x + j] = this.NowBlockMap[i][j];
            }
        }
        return true;//如果所有方块的状态都被写入到BlockMap数组中了，则返回true
    }
    //CalNewBlockInitPos方法用于计算当前下落方块的初始位置，即左上角坐标
    //计算当前游戏区域的中心点，并将其减去方块的偏移量得到
    private Point CalNewBlockInitPos(){
        return new Point(Tetris.BlockWidth / 2 - this.NowBlockMap[0].length / 2, - this.NowBlockMap.length);
    }
    //Initial方法用于初始化游戏状态
    //该方法会清空BlockMap数组，并将分数设置为0
    //它也会初始化当前下落方块和下一个方块的状态，计算出当前下落方块的初始位置
    //调用repaint方法，重新绘制游戏画面
    public void Initial() {
        //清空Map
        for (int i = 0;i < this.BlockMap.length;i ++){
            for (int j = 0;j < this.BlockMap[i].length;j ++){
                this.BlockMap[i][j] = false;
            }
        }
        //清空分数
        this.Score = 0;
        // 初始化第一次生成的方块和下一次生成的方块
        this.NowBlockState = this.CreateNewBlockState();
        this.NowBlockMap = this.getBlockMap(this.NowBlockState);
        this.NextBlockState = this.CreateNewBlockState();
        this.NextBlockMap = this.getBlockMap(this.NextBlockState);
        // 计算方块位置
        this.NowBlockPos = this.CalNewBlockInitPos();
        this.repaint();
    }

    //SetPause方法用于设置游戏是否暂停
    public void SetPause(boolean value){
        this.IsPause = value;
        if (this.IsPause){
            //接受一个boolean值，如果该值为true，则表示需要暂停游戏，此时timer对象会停止计时器的计时
            this.timer.stop();
        }
        else{
            //如果该值为false，则表示需要继续游戏，此时timer对象会重新开始计时器的计时
            this.timer.restart();
        }
        this.repaint();//该方法会调用repaint方法，重新绘制游戏画面
    }
    //CreateNewBlockState方法用于随机产生一个新的方块状态
    //每个状态都是由一个两位数表示的，该方法通过计算方块的总数，并通过取余函数随机地获取这个状态值
    private int CreateNewBlockState() {
        int Sum = Tetris.Shape.length * 4;
        return (int) (Math.random() * 1000) % Sum;
    }
    //getBlockMap方法用于获取指定BlockState（方块状态）的二维布尔数组，表示方块在特定角度的状态
    //此方法会将BlockState参数拆分为两部分：Shape和Arc。其中Shape表示方块的种类，Arc表示方块的旋转角度
    private boolean[][] getBlockMap(int BlockState) {
        int Shape = BlockState / 4;
        int Arc = BlockState % 4;
        System.out.println(BlockState + "," + Shape + "," + Arc);
        return this.RotateBlock(Tetris.Shape[Shape], Arc);
    }

    //这段代码定义了一个RotateBlock方法，用于旋转方块
    private boolean[][] RotateBlock(boolean[][] shape, int time) {
        //该方法接受两个参数，分别是一个表示方块的二维布尔数组和旋转次数
        if(time == 0) {
            //果旋转次数为0，则直接返回原始的方块数组
            return shape;
        }
        //该方法首先计算出方块矩阵的高度和宽度，然后创建一个同样大小的新数组，并将方块数组中的元素依据顺时针方向旋转后放入这个新数组中
        int heigth = shape.length;
        int width = shape[0].length;
        boolean[][] ResultMap = new boolean[heigth][width];
        int tmpH = heigth - 1, tmpW = 0;
        //由于旋转操作会修改原始的方块数组，因此递归调用该方法实现旋转次数的叠加
        for(int i = 0; i < heigth && tmpW < width; i++) {
            for(int j = 0; j < width && tmpH > -1; j++) {
                ResultMap[i][j] = shape[tmpH][tmpW];
                tmpH--;
            }
            tmpH = heigth - 1;
            tmpW++;
        }
        for(int i = 1; i < time; i++) {
            ResultMap = RotateBlock(ResultMap, 0);
        }
        return ResultMap;//最后，返回旋转后的数组
    }
    //用于测试 Tetris 类的 RotateBlock 方法的正确性
    static public void main(String... args) {
        boolean[][] SrcMap = Tetris.Shape[3];
        Tetris.ShowMap(SrcMap);
        Tetris tetris = new Tetris();
        boolean[][] result = tetris.RotateBlock(SrcMap, 1);
        Tetris.ShowMap(result);

    }
    //展示二维布尔数组的静态方法ShowMap
    static private void ShowMap(boolean[][] SrcMap){
        System.out.println("-----");
        for (int i = 0;i < SrcMap.length;i ++){
            for (int j = 0;j < SrcMap[i].length;j ++){
                //遍历整个数组，如果当前位置为真，则输出’*'。否则，表示为假，输出一个空格
                if (SrcMap[i][j])
                    System.out.print("*");
                else
                    System.out.print(" ");
            }
            System.out.println();//在每一行结束后，插入一个换行符
        }
        System.out.println("-----");//再次输出分隔符，以标识这个数组的终结
    }
    //该方法会在游戏区域被重绘时调用
    //使用for循环来绘制游戏区域的墙壁、当前方块、已经固定的方块和下一个方块。
    // 绘制过程中使用NowBlockMap表示当前下落方块的二维布尔数组，使用BlockMap数组表示已经固定的方块。
    // 这些绘图操作主要借助Graphics类提供的fillRect和drawRect方法。
    // 其中fillRect绘制实心矩形，drawRect绘制边框矩形
    public void paintComponent(Graphics g) {
        //调用超类的paintComponent方法以确保已绘制的组件可以被清除
        super.paintComponent(g);
        // 画墙
        for (int i = 0; i < Tetris.BlockHeigth + 1; i++) {
            g.drawRect(0 * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize, Tetris.BlockSize);
            g.drawRect((Tetris.BlockWidth + 1) * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize,
                    Tetris.BlockSize);
        }
        for (int i = 0; i < Tetris.BlockWidth; i++) {
            g.drawRect((1 + i) * Tetris.BlockSize, Tetris.BlockHeigth * Tetris.BlockSize, Tetris.BlockSize,
                    Tetris.BlockSize);
        }
        // 画当前方块
        for (int i = 0; i < this.NowBlockMap.length; i++) {
            for (int j = 0; j < this.NowBlockMap[i].length; j++) {
                if (this.NowBlockMap[i][j])
                    g.fillRect((1 + this.NowBlockPos.x + j) * Tetris.BlockSize, (this.NowBlockPos.y + i) * Tetris.BlockSize,
                            Tetris.BlockSize, Tetris.BlockSize);
            }
        }
        // 画已经固定的方块
        for (int i = 0; i < Tetris.BlockHeigth; i++) {
            for (int j = 0; j < Tetris.BlockWidth; j++) {
                if (this.BlockMap[i][j])
                    g.fillRect(Tetris.BlockSize + j * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize,
                            Tetris.BlockSize);
            }
        }
        //绘制下一个方块
        for (int i = 0;i < this.NextBlockMap.length;i ++){
            for (int j = 0;j < this.NextBlockMap[i].length;j ++){
                if (this.NextBlockMap[i][j])
                    g.fillRect(190 + j * Tetris.BlockSize, 30 + i * Tetris.BlockSize, Tetris.BlockSize, Tetris.BlockSize);
            }
        }
        // 绘制其他信息
        //在游戏区域底部绘制游戏得分。在底部之下绘制作者信息
        g.drawString("游戏分数:" + this.Score, 190, 10);
        for (int i = 0;i < Tetris.AuthorInfo.length;i ++){
            g.drawString(Tetris.AuthorInfo[i], 190, 100 + i * 20);
        }

        //绘制暂停
        //如果游戏处于暂停状态，绘制一个提示窗口，以提醒玩家游戏正在暂停
        if (this.IsPause){
            g.setColor(Color.white);
            g.fillRect(70, 100, 50, 20);
            g.setColor(Color.black);
            g.drawRect(70, 100, 50, 20);
            g.drawString("PAUSE", 75, 113);
        }
    }
    //ClearLines方法，用于清除已经被填满取得得分的行
    //可以通过ClearLines方法获取被清除的行数，有利于更新游戏得分等UI信息
    private int ClearLines(){
        int lines = 0;//定义一个整型变量lines，并初始化为0。这个变量用于记录当前一共清除了多少行方块
        for (int i = 0;i < this.BlockMap.length;i ++){
            boolean IsLine = true;
            for (int j = 0;j < this.BlockMap[i].length;j ++){
                //使用for循环遍历整个BlockMap数组，判断每一行是否已经被填满。
                // 如果该行被填满，则表示玩家已经获得了游戏得分，并且该行需要被清除
                if (!this.BlockMap[i][j]){
                    IsLine = false;
                    break;
                }
            }
            if (IsLine){
               // 如果当前行已经被填满，则将该行上面所有的方块都向下移动一行
                for (int k = i;k > 0;k --){
                    this.BlockMap[k] = this.BlockMap[k - 1];
                }
                this.BlockMap[0] = new boolean[Tetris.BlockWidth];
                lines ++;
            }
        }
        return lines;//结束for循环之后，返回记录清除行数的变量lines
    }

    // 定时器监听
    ActionListener TimerListener = new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            if (Tetris.this.IsTouch(Tetris.this.NowBlockMap, new Point(Tetris.this.NowBlockPos.x, Tetris.this.NowBlockPos.y + 1))){
                //首先判断当前方块是否可以继续下落

                if (Tetris.this.FixBlock()){
                    //如果当前方块已经无法下落的话，就先将它固定在游戏区域内，并检查是否有填满的行
                    Tetris.this.Score += Tetris.this.ClearLines() * 10;
                    Tetris.this.getNextBlock();
                }
                else{
                    //如果没有齐全的方块行，则游戏结束并弹出对话框
                    JOptionPane.showMessageDialog(Tetris.this.getParent(), "GAME OVER");
                    Tetris.this.Initial();
                }
            }
            else{
                Tetris.this.NowBlockPos.y ++;
            }
            Tetris.this.repaint();
        }
    };
    //按键监听
    //当按下向下键时，当前方块会直接跳至最底部
    //当按下向上键时，当前方块会进行顺时针旋转操作
    //当按下向右键时，当前方块会向右移动
    //当按下向左键时，当前方块会向左移动

    java.awt.event.KeyListener KeyListener = new java.awt.event.KeyListener(){
        public void keyPressed(KeyEvent e) {
            if (!IsPause){
                Point DesPoint;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        DesPoint = new Point(Tetris.this.NowBlockPos.x, Tetris.this.NowBlockPos.y + 1);
                        if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)){
                            //如果当前游戏处于暂停状态，这个KeyListener不会做任何响应。
                            // 而如果游戏没有处于暂停状态，则会根据玩家的输入来执行不同的操作
                            Tetris.this.NowBlockPos = DesPoint;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        boolean[][] TurnBlock = Tetris.this.RotateBlock(Tetris.this.NowBlockMap,1);
                        if (!Tetris.this.IsTouch(TurnBlock, Tetris.this.NowBlockPos)){
                            Tetris.this.NowBlockMap = TurnBlock;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        DesPoint = new Point(Tetris.this.NowBlockPos.x + 1, Tetris.this.NowBlockPos.y);
                        if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)){
                            Tetris.this.NowBlockPos = DesPoint;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        DesPoint = new Point(Tetris.this.NowBlockPos.x - 1, Tetris.this.NowBlockPos.y);
                        if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)){
                            Tetris.this.NowBlockPos = DesPoint;
                        }
                        break;
                }
                repaint();
            }
        }
        //实现KeyListener接口时必须要重写的keyReleased方法，用于在玩家松开键盘按键时做出相应的处理
        public void keyReleased(KeyEvent e) {
        }
        //实现KeyListener接口时必须要重写的keyTyped方法，用于在玩家在键盘中输入可打印字符时做出相应的处理
        public void keyTyped(KeyEvent e) {
        }

    };
}