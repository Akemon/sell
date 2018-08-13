package com.hk.sell.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author 何康
 * @date 2018/8/13 19:53
 */
@Entity
@Data
public class ProductInfo {

    @Id
    private String productId;

//    商品名称
    private String productName;

//    单价
    private BigDecimal productPrice;

//    库存
    private Integer productStock;

//    描述
    private String productDescription;

//    小图
    private String productIcon;

//    商品状态，0正常1下架
    private Integer productStatus;

//    类目编号
    private Integer categoryType;
}
