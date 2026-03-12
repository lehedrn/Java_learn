package com.coderlee.designpattern.structural.flyweight.gogame;

/**
 * 围棋游戏演示类
 *
 * @author coderlee
 */
public class GoGameDemo {

    public static void main(String[] args) {
        System.out.println("========== 享元模式演示 - 围棋游戏 ==========\n");

        GoBoard board = new GoBoard();
        board.showBoardSize();

        System.out.println("\n--- 对局开始 ---");

        // 模拟下棋过程（黑子先手）
        String[] moves = {
            "黑", "白", "黑", "白", "黑", "白",
            "黑", "白", "黑", "白", "黑", "白",
            "黑", "白", "黑", "白", "黑", "白"
        };

        int[][] positions = {
            {3, 3}, {3, 4}, {4, 3}, {4, 4}, {5, 3}, {5, 4},
            {10, 10}, {10, 11}, {11, 10}, {11, 11}, {12, 10}, {12, 11},
            {15, 15}, {15, 16}, {16, 15}, {16, 16}, {17, 15}, {17, 16}
        };

        for (int i = 0; i < moves.length; i++) {
            board.placePiece(moves[i], positions[i][0], positions[i][1]);
        }

        System.out.println("\n--- 统计 ---");
        System.out.println("总共下棋 " + moves.length + " 手");
        System.out.println("创建的棋子对象数量：" + GoPieceFactory.getPieceCount() + " 个");
        System.out.println("节省对象数量：" + (moves.length - GoPieceFactory.getPieceCount()) + " 个");

        System.out.println("\n========== 演示结束 ==========");
    }
}
