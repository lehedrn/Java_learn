package com.coderlee.datastructures.sparsearray;

/**
 * 01-稀疏数组
 * 数据结构稀疏数组类，用于实现二维数组与稀疏数组之间的相互转换。
 * 通过列表的形式实现稀疏数组的存储结构。
 * <p>
 * 稀疏数组是一种优化存储方式，特别适用于存储包含大量默认值（如 0）的二维数组。
 * 通过将非零元素的位置和值存储到稀疏数组中，可以显著减少内存占用。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>将普通二维数组转换为稀疏数组：{@link #transform2sparseArray(int[][])}。</li>
 *     <li>将稀疏数组恢复为普通二维数组：{@link #transform22DArray(int[][])}。</li>
 * </ul>
 * </p>
 *
 * @author coderLee
 * @date 2025/10/17 4:18
 */
public class ListSparseArray {

    public static void main(String[] args) { 
        int[][] chessArray = initChessArray(11);
        print2DArray("原始二维数组：", chessArray);
        int[][] sparseArray = transform2sparseArray(chessArray);
        print2DArray("稀疏数组：", sparseArray);
        int[][] chessArray2 = transform22DArray(sparseArray);
        print2DArray("恢复二维数组：", chessArray2);
    }

    /**
     * 稀疏数组恢复二维数组
     *
     * @param sparseArray
     * @return
     */
    private static int[][] transform22DArray(int[][] sparseArray) {
        if (null == sparseArray || sparseArray.length == 0) {
            throw new IllegalArgumentException("输入的稀疏数组不能为空");
        }
        // 创建二维数组
        int[][] chessArray = new int[sparseArray[0][0]][sparseArray[0][1]];
        boolean isFirstRow = true;
        // 遍历稀疏数组
        for (int[] row : sparseArray) {
            // 跳过第一行
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }
            chessArray[row[0]][row[1]] = row[2];
        }
        return chessArray;
    }

    /**
     * 转换二维数组为稀疏数组
     *
     * @param chessArray
     * @return
     */
    private static int[][] transform2sparseArray(int[][] chessArray) {
        if (null == chessArray || chessArray.length == 0) {
            throw new IllegalArgumentException("输入的二维数组不能为空");
        }
        // 统计二维数组有效数据个数
        int count = 0;
        for (int[] row : chessArray) {
            for (int data : row) {
                if (data != 0) {
                    count++;
                }
            }
        }
        System.out.println("二维数组有效数据个数：" + count);
        // 创建稀疏数组
        int[][] sparseArray = new int[count + 1][3];
        int rowCount = chessArray.length;
        int columnCount = chessArray[0].length;
        // 稀疏数组第一行
        sparseArray[0][0] = rowCount;
        sparseArray[0][1] = columnCount;
        sparseArray[0][2] = count;
        // 稀疏数组第二行开始
        int index = 1;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (chessArray[i][j] != 0) {
                    sparseArray[index][0] = i;
                    sparseArray[index][1] = j;
                    sparseArray[index][2] = chessArray[i][j];
                    index++;
                }
            }
        }
        return sparseArray;
    }

    /**
     * 初始化二维数组
     *
     * @param n
     * @return
     */
    private static int[][] initChessArray(int n) {
        int[][] chessArray = new int[n][n];
        chessArray[1][2] = 1;
        chessArray[2][3] = 2;
        return chessArray;
    }

    /**
     * 打印二维数组
     *
     * @param array
     */
    private static void print2DArray(String content, int[][] array) {
        if (null != content) {
            System.out.println(content);
        }
        for (int[] row : array) {
            for (int data : row) {
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }
    }

}
