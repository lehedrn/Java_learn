package com.coderlee.designpattern.structural.flyweight.gogame;

/**
 * 具体享元：黑子
 * <p>
 * 黑子是共享的，所有黑子共用这个对象
 * 内在状态：颜色（黑色）
 * </p>
 *
 * @author coderlee
 */
public class BlackPiece implements GoPiece {

    private final String color = "黑子";

    @Override
    public void display(int x, int y) {
        System.out.printf("⚫ 黑子 位置：(%d, %d)\n", x, y);
    }

    @Override
    public String getColor() {
        return color;
    }
}
