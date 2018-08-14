package com.hk.sell.service;

import com.hk.sell.dto.OrderDTO;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 何康
 * @date 2018/8/14 20:36
 */
public interface OrderService {

    /** 创建订单 */
    OrderDTO create(OrderDTO orderDTO);

    /** 查询订单 */
    OrderDTO findOne(String orderId);

    /** 查询订单列表 */
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /** 取消订单 */
    OrderDTO cancel(OrderDTO orderDTO);

    /** 完结订单 */
    OrderDTO finish(OrderDTO orderDTO);

    /** 支付订单 */
    OrderDTO paid(OrderDTO orderDTO);

}
