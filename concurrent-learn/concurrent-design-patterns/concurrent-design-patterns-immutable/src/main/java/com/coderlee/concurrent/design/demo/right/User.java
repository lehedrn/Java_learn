package com.coderlee.concurrent.design.demo.right;

/**
 * 不可变用户类，用于表示一个用户的信息。
 * <p>
 * 该类是线程安全的，因为其状态在创建后不能被修改。
 * </p>
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/immutable.html">Immutable Objects in Java</a>
 */
public final class User {

    /**
     * 用户的姓名。
     */
    private final String name;

    /**
     * 用户的身份证号。
     */
    private final Long idCard;

    /**
     * 构造一个新的用户实例。
     *
     * @param name   用户的姓名
     * @param idCard 用户的身份证号
     */
    public User(String name, Long idCard) {
        this.name = name;
        this.idCard = idCard;
    }

    /**
     * 返回该用户的字符串表示形式。
     *
     * @return 用户信息的字符串表示
     */
    @Override
    public String toString() {
        // 返回包含用户姓名和身份证号的字符串
        return "User [name=" + name + ", idCard=" + idCard + "]";
    }
}

