package com.hk.sell.service.impl;

import com.hk.sell.bean.OrderDetail;
import com.hk.sell.dto.OrderDTO;
import com.hk.sell.enums.OrderStatusEnum;
import com.hk.sell.enums.PayStatusEnum;
import com.hk.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 何康
 * @date 2018/8/15 20:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void create() {
        OrderDTO orderDTO =new OrderDTO();
        orderDTO.setBuyerName("何康");
        orderDTO.setBuyerAddress("深圳");
        orderDTO.setBuyerPhone("4554545454");
        orderDTO.setBuyerOpenid("110110");

        //购物车
        List<OrderDetail> orderDetailList =new ArrayList<>();
        OrderDetail orderDetail =new OrderDetail();
        orderDetail.setProductId("123");
        orderDetail.setProductQuantity(2);
        orderDetailList.add(orderDetail);

        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result =orderService.create(orderDTO);
        log.info("【创建订单】 result ={}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() {
        OrderDTO result =orderService.findOne("1534337318476792522");
        log.info("result={}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findList() {
        PageRequest request =new PageRequest(0,5);
        Page<OrderDTO> orderDTOPage =orderService.findList("110110",request);
        Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO =orderService.findOne("1534337318476792522");
        OrderDTO result =orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO =orderService.findOne("1534337318476792522");
        OrderDTO result =orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO =orderService.findOne("1534337318476792522");
        OrderDTO result =orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getPayStatus());
    }
}