package com.coderlee.designpattern.structural.flyweight.gogame;

/**
 * 具体享元：白子
 * <p>
 * 白子是共享的，所有白子共用这个对象
 * 内在状态：颜色（白色）
 * </p>
 *
 * @author coderlee
 */
public class WhitePiece implements GoPiece {

    private final String color = "白子";

    @Override
    public void display(int x, int y) {
        System.out.printf("⚪ 白子 位置：(%d, %d)\n", x, y);
    }

    @Override
    public String getColor() {
        return color;
    }
}
