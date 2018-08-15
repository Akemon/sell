package com.hk.sell.dto;

import lombok.Data;

/**
 * @author 何康
 * @date 2018/8/15 19:49
 */
@Data
public class CartDTO {
//    商品id
    private String productId;

//    商品数量
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
