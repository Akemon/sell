//这个包意思是数据传输对象(Data Transfer Object) ，专用于Controller与bean层的数据交换
package com.hk.sell.dto;

import com.hk.sell.bean.OrderDetail;
import com.hk.sell.enums.OrderStatusEnum;
import com.hk.sell.enums.PayStatusEnum;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 何康
 * @date 2018/8/14 20:42
 */
@Data
public class OrderDTO {


    //    订单id
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
    private Integer orderStatus;

    //    支付状态，默认为0未支付
    private Integer payStatus;

    private List<OrderDetail> orderDetailList;
}
