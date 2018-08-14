package com.hk.sell.enums;

/**
 * @author 何康
 * @date 2018/8/14 21:00
 */

import lombok.Getter;

/**
 * 商品异常返回值的枚举
 */
@Getter
public enum ResultEnum {

    PRODUCT_NOT_EXIST(10,"商品不存在"),
    ;
    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
