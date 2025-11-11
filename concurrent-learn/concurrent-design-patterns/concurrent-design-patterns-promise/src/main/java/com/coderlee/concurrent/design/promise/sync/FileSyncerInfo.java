package com.coderlee.concurrent.design.promise.sync;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息数据封装类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileSyncerInfo {

    // 文件字节流
    private byte[] file;

    // 文件名称
    private String fileName;

    // 文件大小（单位：字节）
    private Integer fileSize;
}
