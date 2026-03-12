package com.coderlee.designpattern.structural.flyweight.gogame;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂：围棋棋子工厂
 * <p>
 * 管理共享的棋子对象，只创建两个对象（黑子和白子）
 * </p>
 *
 * @author coderlee
 */
public class GoPieceFactory {

    private static final Map<String, GoPiece> pieceMap = new HashMap<>();

    /**
     * 获取棋子
     * @param color 颜色
     * @return 棋子对象
     */
    public static GoPiece getPiece(String color) {
        if (pieceMap.containsKey(color)) {
            System.out.println("  [复用] " + color + " 子对象");
            return pieceMap.get(color);
        }

        GoPiece piece;
        if ("黑".equals(color)) {
            piece = new BlackPiece();
        } else if ("白".equals(color)) {
            piece = new WhitePiece();
        } else {
            throw new IllegalArgumentException("不支持的棋子颜色：" + color);
        }

        pieceMap.put(color, piece);
        System.out.println("  [创建] " + color + " 子对象");
        return piece;
    }

    /**
     * 获取已创建的棋子数量
     * @return 数量
     */
    public static int getPieceCount() {
        return pieceMap.size();
    }
}
