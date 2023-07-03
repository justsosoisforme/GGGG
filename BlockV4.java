package com.hlju.Tetris;

public class BlockV4 {
    static final boolean[][][] Shape = {
            //Shape 是一个三维布尔数组，第一维表示七种俄罗斯方块中的一种，第二维和第三维表示该俄罗斯方块的各个格子是否被占用
            //如果某个格子被占用，则为 true；反之为 false。
            // I
            //I 形俄罗斯方块有四行四列，只有第二行上所有格子被占用；
            {
                    { false, false, false, false },
                    { true, true, true, true },
                    { false, false, false, false },
                    { false, false, false, false }
            },
            // J
            //J 形俄罗斯方块有三行三列，第一行只有第一个格子被占用，第二行和第三行各有两个格子被占用；
            {
                    { true, false, false },
                    { true, true, true },
                    { false, false, false }
            },
            // L
            //L 形俄罗斯方块有三行三列，第一行只有第三个格子被占用，第二行和第三行各有两个格子被占用；
            {
                    { false, false, true },
                    { true, true, true },
                    { false, false, false }
            },
            // O
            //O 形俄罗斯方块有二行两列，所有格子都被占用；
            {
                    { true, true },
                    { true, true }
            },
            // S
            //S 形俄罗斯方块有三行三列，第一行第二、三个格子被占用，第二行第一、二个格子被占用，第三行第三个格子被占用；
            {
                    { false, true, true },
                    { true, true, false },
                    { false, false, false }
            },
            // T
            // T 形俄罗斯方块有三行三列，第一行只有第二个格子被占用，第二行三个格子都被占用，第三行没有格子被占用；
            {
                    { false, true, false },
                    { true, true, true },
                    { false, false, false }
            },
            // Z
            //Z 形俄罗斯方块有三行三列，第一行第一、二个格子被占用，第二行第二、三个格子被占用，第三行没有格子被占用。
            {
                    { true, true, false },
                    { false, true, true },
                    { false, false, false }
            } };
}