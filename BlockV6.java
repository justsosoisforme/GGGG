package com.hlju.Tetris;

public class BlockV6 {
    static final boolean[][][] Shape = {
//静态final boolean数组，用来存储方块的不同形状
//数组的元素为8个布尔型二维数组，代表不同形状的方块
            //直线形状
            {
                    { false, false, false, false, false, false },
                    { false, false, false, false, false, false },
                    { true, true, true, true, true, true },
                    { false, false, false, false, false, false },
                    { false, false, false, false, false, false },
                    { false, false, false, false, false, false }
            },

            {
                    { true, true, true },
                    { true, true, true },
                    { false, false, false }
            },
            //剩下的6个元素则代表各种不同的L形状和Z形状
            {
                    { true, true, true , false },
                    { false, false, true, false },
                    { false, false, true, false },
                    { false, false, true, false }
            },

            {
                    { false, true, true , true },
                    { false, true, false, false },
                    { false, true, false, false },
                    { false, true, false, false }
            },

            {
                    { false, false, true, false, false },
                    { false, false, true, false, false },
                    { false, true, true, false, false },
                    { false, true, false, false, false },
                    { false, true, false, false, false }
            },

            {
                    { false, true, false, false, false },
                    { false, true, false, false, false },
                    { false, true, true, false, false },
                    { false, false, true, false, false },
                    { false, false, true, false, false }
            },

            {
                    { false, true, true , true },
                    { false, false, true, false },
                    { false, false, true, false },
                    { false, false, true, false }
            },

            {
                    { false, false, false, false, false },
                    { true, true, true, true, true },
                    { false, false, true, false, false },
                    { false, false, true, false, false },
                    { false, false, false, false, false }
            }
    };
}