package com.hk.sell.repository;

import com.hk.sell.bean.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 何康
 * @date 2018/8/14 19:51
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
    /**
     * 通过买家微信id分页查询用户的订单
     */
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
