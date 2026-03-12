package com.coderlee.designpattern.structural.flyweight.gogame;

/**
 * 围棋棋盘
 * <p>
 * 记录棋盘上的所有棋子位置
 * </p>
 *
 * @author coderlee
 */
public class GoBoard {

    private static final int BOARD_SIZE = 19;

    /**
     * 下棋
     * @param color 颜色
     * @param x x 坐标
     * @param y y 坐标
     */
    public void placePiece(String color, int x, int y) {
        GoPiece piece = GoPieceFactory.getPiece(color);
        piece.display(x, y);
    }

    /**
     * 显示棋盘大小
     */
    public void showBoardSize() {
        System.out.println("棋盘大小：" + BOARD_SIZE + " × " + BOARD_SIZE);
    }
}
