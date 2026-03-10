package com.coderlee.designpattern.structural.proxy;

/**
 * 用户服务接口
 * <p>
 * 定义用户相关的业务操作方法
 * 该接口将用于静态代理和 JDK 动态代理
 * </p>
 *
 * @author coderlee
 */
public interface UserService {
    /**
     * 保存用户信息
     *
     * @param user 要保存的用户对象
     */
    void saveUser(User user);
    
    /**
     * 根据 ID 获取用户信息
     *
     * @param id 用户 ID
     * @return 用户对象
     */
    User getUserById(Long id);
}
