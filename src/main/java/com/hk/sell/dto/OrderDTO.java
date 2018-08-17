//这个包意思是数据传输对象(Data Transfer Object) ，专用于Controller与bean层的数据交换
package com.hk.sell.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hk.sell.bean.OrderDetail;
import com.hk.sell.enums.OrderStatusEnum;
import com.hk.sell.enums.PayStatusEnum;
import com.hk.sell.util.serializer.Data2LongSerializer;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 何康
 * @date 2018/8/14 20:42
 */
@Data
@DynamicUpdate
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
/***
 * 这个注解当成员属性为空时不会返回null，属性直接不显示，但是不直接加，使用全局配置更做优化
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonSerialize(using = Data2LongSerializer.class)
    private Date createTime;

    @JsonSerialize(using = Data2LongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList;


}
