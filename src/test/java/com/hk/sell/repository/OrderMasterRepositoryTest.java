package com.hk.sell.repository;

import com.hk.sell.bean.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author 何康
 * @date 2018/8/14 20:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    private final String OPENID ="110110";

    @Test
    @Transactional
    public void save(){
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId("1234567");
        orderMaster.setBuyerName("何康");
        orderMaster.setBuyerPhone("18218081379");
        orderMaster.setBuyerAddress("不能告诉你");
        orderMaster.setBuyerOpenid("110110");
        orderMaster.setOrderAmount(new BigDecimal(5000));
        OrderMaster order = orderMasterRepository.save(orderMaster);
        Assert.assertNotNull(order);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest request =new PageRequest(0,5);
        Page<OrderMaster> result = orderMasterRepository.findByBuyerOpenid(OPENID, request);
        System.out.println(result.getTotalElements());
        Assert.assertNotEquals(0,result.getTotalElements());
    }
}