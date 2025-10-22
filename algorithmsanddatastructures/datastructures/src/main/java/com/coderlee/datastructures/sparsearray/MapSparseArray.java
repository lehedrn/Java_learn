package com.coderlee.datastructures.sparsearray;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用 HashMap 实现稀疏数组的转换和恢复。
 * 该类演示了如何将二维数组转换为稀疏数组，以及如何将稀疏数组还原为原始二维数组。
 */
public class MapSparseArray {

    /**
     * 主方法，程序入口点。
     * 演示稀疏数组的转换和恢复过程：
     * 1. 初始化一个二维数组并打印；
     * 2. 将二维数组转换为稀疏数组并打印；
     * 3. 将稀疏数组还原为二维数组并打印。
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) { 
        // 初始化一个 11x11 的二维数组
        int[][] chessArray = initChessArray(11);
        // 打印原始二维数组
        print2DArray("原始二维数组：", chessArray);

        // 将二维数组转换为稀疏数组
        Map<String, Integer> sparseArray = transform2sparseArray(chessArray);
        System.out.println("稀疏数组：");
        System.out.println(sparseArray); // 打印稀疏数组

        // 将稀疏数组还原为二维数组
        int[][] chessArray2 = transform22DArray(sparseArray);
        // 打印恢复后的二维数组
        print2DArray("恢复二维数组：", chessArray2);
    }

    /**
     * 将稀疏数组还原为二维数组。
     *
     * @param sparseArray 稀疏数组，存储了二维数组的非零元素及其位置信息。
     *                    稀疏数组中必须包含 "row" 和 "column" 键，分别表示原始二维数组的行数和列数。
     * @return 还原后的二维数组。
     * @throws IllegalArgumentException 如果输入的稀疏数组为空或缺少必要信息，则抛出异常。
     */
    private static int[][] transform22DArray(Map<String, Integer> sparseArray) {
        if (null == sparseArray || sparseArray.size() == 0) {
            throw new IllegalArgumentException("输入的稀疏数组不能为空"); // 输入校验
        }
        // 获取原始二维数组的行数和列数
        int[][] chessArray = new int[sparseArray.get("row")][sparseArray.get("column")];
        for (Map.Entry<String, Integer> entry : sparseArray.entrySet()) {
            if (!"row".equals(entry.getKey()) && !"column".equals(entry.getKey())) {
                String[] index = entry.getKey().split(","); // 解析索引信息
                int value = entry.getValue(); // 获取对应的值
                chessArray[Integer.parseInt(index[0])][Integer.parseInt(index[1])] = value; // 恢复到二维数组
            }
        }
        return chessArray; // 返回还原后的二维数组
    }

    /**
     * 将二维数组转换为稀疏数组。
     *
     * @param chessArray 原始二维数组，存储了棋盘数据。
     *                   只有非零元素会被存储到稀疏数组中。
     * @return 转换后的稀疏数组。
     *         稀疏数组使用 HashMap 存储，键为 "row" 和 "column" 表示二维数组的行列数，
     *         其余键为 "i,j" 格式的字符串，表示非零元素的位置。
     * @throws IllegalArgumentException 如果输入的二维数组为空，则抛出异常。
     */
    private static Map<String, Integer> transform2sparseArray(int[][] chessArray) {
        if (null == chessArray || chessArray.length == 0) {
            throw new IllegalArgumentException("输入的二维数组不能为空"); // 输入校验
        }
        Map<String, Integer> map = new HashMap<>(); // 创建稀疏数组
        map.put("row", chessArray.length); // 存储二维数组的行数
        map.put("column", chessArray[0].length); // 存储二维数组的列数
        for (int i = 0; i < chessArray.length; i++) {
            for (int j = 0; j < chessArray[i].length; j++) {
                if (0 != chessArray[i][j]) { // 只存储非零元素
                    map.put(i + "," + j, chessArray[i][j]); // 键为 "i,j"，值为对应元素
                }
            }
        }
        return map; // 返回稀疏数组
    }

    /**
     * 初始化一个 n x n 的二维数组，并设置部分初始值。
     *
     * @param n 二维数组的行数和列数。
     * @return 初始化后的二维数组。
     */
    private static int[][] initChessArray(int n) {
        int[][] chessArray = new int[n][n]; // 创建 n x n 的二维数组
        chessArray[1][2] = 1; // 设置特定位置的值
        chessArray[2][3] = 2; // 设置特定位置的值
        return chessArray; // 返回初始化后的数组
    }

    /**
     * 打印二维数组的内容。
     *
     * @param content 打印前的描述内容（可选）。
     * @param array   需要打印的二维数组。
     */
    private static void print2DArray(String content, int[][] array) {
        if (null != content) {
            System.out.println(content); // 打印描述内容
        }
        for (int[] row : array) { // 遍历每一行
            for (int data : row) { // 遍历每一列
                System.out.printf("%d\t", data); // 打印每个元素
            }
            System.out.println(); // 换行
        }
    }
}