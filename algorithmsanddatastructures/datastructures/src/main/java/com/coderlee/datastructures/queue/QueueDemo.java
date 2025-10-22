package com.coderlee.datastructures.queue;

import java.util.Scanner;

/**
 * 队列操作的演示类。
 * 该类提供了一个主方法，用于创建队列实例并通过控制台与用户交互。
 * 支持两种队列实现：普通数组队列和循环队列。
 */
public class QueueDemo {

    /**
     * 主方法，程序入口点。
     * 创建一个队列实例（可以是普通数组队列或循环队列），并调用 [print] 方法进行操作演示。
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个数组队列实例（已注释）
        // ArrayQueue queue = new ArrayQueue(3);

        // 创建一个循环队列实例
        CircleArrayQueue queue = new CircleArrayQueue(3);

        // 调用工具方法打印菜单并执行用户操作
        print(queue);        
    }

    /**
     * 打印队列操作菜单并通过控制台与用户交互。
     * 
     * @param queue 需要操作的队列实例，必须实现 [MyQueue] 接口。
     *              该方法会根据用户输入执行相应的队列操作（如显示队列、添加数据等），
     *              直到用户选择退出程序。
     */
    public static void print(MyQueue queue) {
        // 打印测试信息，显示当前队列的实现类名称
        System.out.println("测试" + queue.getClass().getSimpleName());
        
        char key = ' '; // 用户输入的操作选项
        Scanner scanner = new Scanner(System.in); // 用于读取用户输入
        boolean loop = true; // 控制循环的标志，true 表示继续运行，false 表示退出

        // 循环输出菜单，直到用户选择退出
        while (loop) {
            // 打印操作菜单
            System.out.println("s(show): 显示队列");
            System.out.println("e(exit): 退出程序");
            System.out.println("a(add): 添加数据到队列");
            System.out.println("g(get): 从队列取出数据");
            System.out.println("h(head): 查看队列头的数据");

            key = scanner.next().charAt(0); // 读取用户输入的第一个字符

            // 根据用户输入执行对应的操作
            switch (key) {
                case 's': // 显示队列内容
                    queue.show();
                    break;
                case 'a': // 添加数据到队列
                    try {
                        System.out.println("输出一个数"); // 提示用户输入一个整数
                        int value = scanner.nextInt(); // 读取用户输入的整数值
                        queue.add(value); // 将值添加到队列中
                    } catch (Exception e) {
                        System.out.println(e.getMessage()); // 捕获异常并打印错误信息
                    }
                    break;
                case 'g': // 从队列取出数据
                    try {
                        int res = queue.get(); // 尝试从队列中获取数据
                        System.out.printf("取出的数据是%d\n", res); // 输出取出的数据
                    } catch (Exception e) {
                        System.out.println(e.getMessage()); // 捕获异常并打印错误信息
                    }
                    break;
                case 'h': // 查看队列头的数据
                    try {
                        int res = queue.head(); // 获取队列头的数据
                        System.out.printf("队列头的数据是%d\n", res); // 输出队列头的数据
                    } catch (Exception e) {
                        System.out.println(e.getMessage()); // 捕获异常并打印错误信息
                    }
                    break;
                case 'e': // 退出程序
                    scanner.close(); // 关闭扫描器以释放资源
                    loop = false; // 设置循环标志为 false，退出循环
                    break;
                default: // 处理无效输入
                    System.out.println("无效的输入，请重新选择！"); // 提示用户输入无效
                    break;
            }
        }
        System.out.println("程序退出~~"); // 提示用户程序已退出
    }
}