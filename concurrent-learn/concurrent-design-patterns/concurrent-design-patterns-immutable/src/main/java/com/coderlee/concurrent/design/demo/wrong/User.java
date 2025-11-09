package com.coderlee.concurrent.design.demo.wrong;

/**
 * 一个用户实体类(非不可变类)。
 * <p>
 * 该类主要用于存储用户的姓名和身份证信息。由于该类的字段是可变的（mutable），
 * 因此在并发场景下可能会引发线程安全问题，建议考虑使用不可变对象设计模式来增强线程安全性。
 * </p>
 */
public class User {

    /**
     * 用户的姓名。
     */
    private String name;

    /**
     * 用户的身份证号码。
     */
    private Long idCard;

    /**
     * 设置用户的姓名和身份证号码。
     * 
     * @param name   用户的姓名
     * @param idCard 用户的身份证号码
     */
    public void set(String name, Long idCard) {
        // 设置用户的姓名
        this.name = name;
        
        // 设置用户的身份证号码
        this.idCard = idCard;
    }

    /**
     * 返回该用户对象的字符串表示形式。
     * 
     * @return 包含用户姓名和身份证号码的字符串
     */
    @Override
    public String toString() {
        return "User [name=" + name + ", idCard=" + idCard + "]";
    }
}