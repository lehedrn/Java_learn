package com.coderlee.concurrent.design.demo.right;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

/**
 * 检票服务测试类
 *
 * 该类用于测试 {@link TicketCheck} 类的功能，验证其线程安全性和不可变对象模式的正确性。
 * 通过测试用例验证用户信息的添加和获取功能，以及通过 {@link TicketCheck#getUserMap()}
 * 获取的不可修改映射表是否能正确防止外部修改。
 *
 * @author coderlee
 * @see TicketCheck
 */
public class TicketCheckTest {

    /**
     * 测试检票服务的基本功能
     *
     * 该测试用例验证以下功能：
     * 1. 添加用户信息到检票服务中
     * 2. 打印当前用户映射表的内容
     * 3. 验证通过 {@link TicketCheck#getUserMap()} 获取的映射表是不可修改的
     *
     * 测试流程：
     * 1. 创建 {@link TicketCheck} 实例
     * 2. 添加两个用户信息
     * 3. 第一次打印用户映射表
     * 4. 尝试通过获取的用户映射表添加新用户（应该会抛出异常）
     * 5. 第二次打印用户映射表验证数据未被修改
     *
     * @see TicketCheck
     * @see TicketCheck#updateUser(String, User)
     * @see TicketCheck#getUserMap()
     * @see TicketCheck#printUserMap()
     */
    @Test
    public void testTicketCheck() {
        // 创建检票服务实例
        TicketCheck ticketCheck = new TicketCheck();

        // 添加第一个用户信息
        ticketCheck.updateUser("1001", new User("张三", 1001L));

        // 添加第二个用户信息
        ticketCheck.updateUser("1002", new User("李四", 1002L));

        // 第一次打印userMap，显示当前所有用户信息
        ticketCheck.printUserMap();

        // 获取用户映射表的不可修改视图
        Map<String, User> userMap = ticketCheck.getUserMap();

        // 尝试通过不可修改映射表添加新用户，这一步应该会抛出 UnsupportedOperationException 异常
        userMap.put("1003", new User("王五", 1003L));

        // 第二次打印userMap，验证数据未被外部修改
        ticketCheck.printUserMap();
    }
}
