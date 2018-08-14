package com.hk.sell.bean;

import com.hk.sell.enums.OrderStatusEnum;
import com.hk.sell.enums.PayStatusEnum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 何康
 * @date 2018/8/14 19:28
 */
@Entity
@Data
public class OrderMaster {

//    订单id
    @Id
    private String orderId;

//    买家名字
    private String buyerName;

//    买家电话
    private String buyerPhone;

//    买家地址
    private String buyerAddress;

//    买家微信id
    private String buyerOpenid;

//    订单总金额
    private BigDecimal orderAmount;

//    订单状态，默认为新订单
    private Integer orderStatus= OrderStatusEnum.NEW.getCode();

//    支付状态，默认为0未支付
    private Integer payStatus= PayStatusEnum.WAIT.getCode();

    //忽略与数据库的关联（不建议直接写在这里，这是与数据库交互的地方）
//    @Transient
//    private List<OrderDetail> orderDetailList;
}
