package com.hlju.Tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TetrisApp extends JFrame {
    //确保在对象序列化和反序列化中被引用到，并且不能被修改
    Tetris tetris = new Tetris();
    public TetrisApp() {
        this.setLocationRelativeTo(null);//设置窗口居中显示的方法
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗口的关闭按钮的方式
        this.setSize(280, 350);
        this.setTitle("Tetris ");
        this.setResizable(false);//设置窗口不可调整大小，使用setResizable方法设置为false
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        JMenu gameMenu = new JMenu("游戏");
        JMenuItem newGameItem = gameMenu.add("新游戏");
        newGameItem.addActionListener(this.NewGameAction);//this关键字用来引用外部类的实例
        //当newGameItem被点击时，ActionListener对象就会回调外部类（即this）的NewGameAction方法
        JMenuItem pauseItem = gameMenu.add("暂停");
        pauseItem.addActionListener(this.PauseAction);
        JMenuItem continueItem = gameMenu.add("继续");
        continueItem.addActionListener(this.ContinueAction);
        JMenuItem exitItem = gameMenu.add("退出");
        exitItem.addActionListener(this.ExitAction);
        JMenu modeMenu = new JMenu("模式");
        JMenuItem v4Item = modeMenu.add("4方块");
        v4Item.addActionListener(this.v4Action);
        JMenuItem v6Item = modeMenu.add("6方块");
        v6Item.addActionListener(this.v6Action);
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = helpMenu.add("关于");
        aboutItem.addActionListener(this.AboutAction);
        menu.add(gameMenu);
        menu.add(modeMenu);
        menu.add(helpMenu);
        this.add(this.tetris);
        this.tetris.setFocusable(true);//将 tetris 对象设置为可聚焦，这样用户就可以通过键盘输入来控制游戏
    }

   static public void main(String... args) {
        //main方法的参数是一个名为args的字符串数组，它包含了传递给程序的各个命令行参数。在Java中，可变参数可以使用"…"语法实现
       TetrisApp tetrisApp = new TetrisApp();
        tetrisApp.setVisible(true);
    }
//定义6个内部类"NewGameAction"、"PauseAction"、"ContinueAction"、"ExitAction"、"AboutAction" 和 "v4Action"、"v6Action"
//内部类访问了外部类 "TetrisApp" 中的成员变量和方法
    ActionListener NewGameAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            TetrisApp.this.tetris.Initial();
            //在用户单击新游戏按钮时会调用tetris.Initial()方法
        }
    };

    ActionListener PauseAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            TetrisApp.this.tetris.SetPause(true);
            //单击暂停按钮时会调用tetris.SetPause(true)方法
        }
    };

    ActionListener ContinueAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            TetrisApp.this.tetris.SetPause(false);
            //单击继续按钮时会调用tetris.SetPause(true)方法
        }
    };

    ActionListener ExitAction = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
            //用户单击退出按钮时停止程序执行
        }
    };

    ActionListener AboutAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(TetrisApp.this, "Tetris Remake Ver 1.0", "关于", JOptionPane.WARNING_MESSAGE);
        }
    };

    ActionListener v4Action = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            TetrisApp.this.tetris.SetMode("v4");
        }
    };

    ActionListener v6Action = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            TetrisApp.this.tetris.SetMode("v6");
        }
    };
}