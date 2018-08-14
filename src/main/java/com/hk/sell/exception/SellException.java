package com.hk.sell.exception;

import com.hk.sell.enums.ResultEnum;

/**
 * @author 何康
 * @date 2018/8/14 20:58
 */
public class SellException extends  RuntimeException{

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
