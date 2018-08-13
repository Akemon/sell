package com.hk.sell.enums;

import lombok.Getter;

/**
 * 商品状态
 * @author 何康
 * @date 2018/8/13 20:49
 */
@Getter
public enum ProductStatusEnum {
    UP(0,"在架"),
    Down(1,"下架")
    ;
    private Integer code;

    private String message;

    ProductStatusEnum(Integer code,String message) {
        this.code = code;
        this.message =message;
    }

}
