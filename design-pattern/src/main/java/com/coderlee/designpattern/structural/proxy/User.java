package com.coderlee.designpattern.structural.proxy;

import lombok.Data;

/**
 * 用户实体类
 * <p>
 * 用于在代理模式中传输用户数据
 * </p>
 *
 * @author coderlee
 */
@Data
public class User {
    /** 用户 ID */
    private Long id;
    
    /** 用户名称 */
    private String name;
}
