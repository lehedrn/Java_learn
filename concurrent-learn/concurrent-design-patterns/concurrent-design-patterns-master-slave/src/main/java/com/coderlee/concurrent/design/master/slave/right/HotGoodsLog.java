package com.coderlee.concurrent.design.master.slave.right;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品热度日志实体类，封装单条商品访问记录。
 *
 * @author coderlee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotGoodsLog {

    /**
     * 商品ID
     */
    private String goodsId;

    /**
     * 访问时间戳
     */
    private String timeStamp;
}
