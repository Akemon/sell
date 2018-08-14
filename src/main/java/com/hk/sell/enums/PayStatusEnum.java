package com.hk.sell.enums;

import lombok.Getter;

/**
 * @author 何康
 * @date 2018/8/14 19:36
 */
@Getter
public enum PayStatusEnum {
    WAIT(0,"未支付"),
    SUCCESS(1,"支付成功")
    ;


    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
