package com.hk.sell.repository;

import com.hk.sell.bean.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 何康
 * @date 2018/8/14 20:07
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {
    /**
     * 通过订单id查询订单详情(一对多的关系)
     */
    List<OrderDetail> findByOrderId(String orderId);
}
