package com.coderlee.concurrent.design.promise.sync;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件同步配置参数实体类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileSyncerConfig {

    // 服务器地址
    private String serverAddress;

    // 登录用户名
    private String username;

    // 登录密码
    private String password;

    // 同步目标目录
    private String serverDir;
}

