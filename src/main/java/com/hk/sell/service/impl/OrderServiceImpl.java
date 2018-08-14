package com.hk.sell.service.impl;

import com.hk.sell.bean.OrderDetail;
import com.hk.sell.bean.OrderMaster;
import com.hk.sell.bean.ProductInfo;
import com.hk.sell.dto.OrderDTO;
import com.hk.sell.enums.ResultEnum;
import com.hk.sell.exception.SellException;
import com.hk.sell.repository.OrderDetailRepository;
import com.hk.sell.repository.OrderMasterRepository;
import com.hk.sell.service.OrderService;
import com.hk.sell.service.ProductService;
import com.hk.sell.util.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Key;

/**
 * @author 何康
 * @date 2018/8/14 20:51
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount =new BigDecimal(0);
        //   1.查询商品(数量，价格)
        for(OrderDetail orderDetail :orderDTO.getOrderDetailList()){
            ProductInfo product = productService.findOne(orderDetail.getProductId());
            if(product == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //   2.计算总价
            orderAmount = orderDetail.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //订单详情入库
            orderDetail.setOrderId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            //商品图片与及名称等等需要拷贝
            BeanUtils.copyProperties(product,orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        //   3.写入订单数据数据库
        OrderMaster orderMaster =new OrderMaster();
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMasterRepository.save(orderMaster);
        //   4.扣库存


        return null;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        return null;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }
}
