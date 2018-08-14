package com.hk.sell.repository;

import com.hk.sell.bean.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 何康
 * @date 2018/8/14 20:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    @Test
    public void save(){
        OrderDetail orderDetail =new OrderDetail();
        orderDetail.setDetailId("1234567");
        orderDetail.setOrderId("1234567");
        orderDetail.setProductIcon("http://xxxx.jpg");
        orderDetail.setProductId("1234");
        orderDetail.setProductName("芒果冰");
        orderDetail.setProductPrice(new BigDecimal(5));
        orderDetail.setProductQuantity(10);
        OrderDetail order = orderDetailRepository.save(orderDetail);
        Assert.assertNotNull(order);
    }

    @Test
    public void findByOrderId() {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId("1234567");
        Assert.assertNotEquals(0,orderDetailList.size());
    }
}