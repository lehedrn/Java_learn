package com.coderlee.concurrent.design.pc.right;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息类，封装文件的基本信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件内容
     */
    private byte[] file;
}
