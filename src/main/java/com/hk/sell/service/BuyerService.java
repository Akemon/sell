package com.hk.sell.service;

import com.hk.sell.dto.OrderDTO;

/**
 * @author 何康
 * @date 2018/8/17 21:03
 */
public interface BuyerService {
    //查询一个订单
    OrderDTO findOrderOne(String openid,String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid,String orderId);
}
