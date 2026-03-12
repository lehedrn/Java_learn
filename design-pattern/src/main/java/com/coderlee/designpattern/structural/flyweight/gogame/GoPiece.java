package com.coderlee.designpattern.structural.flyweight.gogame;

/**
 * 享元接口：围棋棋子
 *
 * @author coderlee
 */
public interface GoPiece {
    /**
     * 显示棋子
     * @param x x 坐标
     * @param y y 坐标
     */
    void display(int x, int y);

    /**
     * 获取棋子颜色
     * @return 颜色
     */
    String getColor();
}
