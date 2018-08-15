package com.hk.sell.service.impl;

import com.hk.sell.bean.OrderDetail;
import com.hk.sell.bean.OrderMaster;
import com.hk.sell.bean.ProductInfo;
import com.hk.sell.dto.CartDTO;
import com.hk.sell.dto.OrderDTO;
import com.hk.sell.enums.OrderStatusEnum;
import com.hk.sell.enums.PayStatusEnum;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        //生成订单id
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount =new BigDecimal(0);
        //   1.查询商品(数量，价格)
        for(OrderDetail orderDetail :orderDTO.getOrderDetailList()){
            ProductInfo product = productService.findOne(orderDetail.getProductId());
            if(product == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //   2.计算总价
            orderAmount = product.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //商品图片与及名称等等需要拷贝(注意：属性拷贝的时候会覆盖所有属性的值，包括null)
            BeanUtils.copyProperties(product,orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            //订单详情入库
            orderDetailRepository.save(orderDetail);
        }

        //   3.写入订单数据数据库
        OrderMaster orderMaster =new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        //   4.扣库存
        //java 8新特性利用lambda表达式
        List<CartDTO> cartDTOList =
                orderDTO.getOrderDetailList().stream().map(e ->
                    new CartDTO(e.getProductId(),e.getProductQuantity())
                ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster =orderMasterRepository.findById(orderId).orElse(null);
        if(orderMaster == null) throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        List<OrderDetail>  orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(orderDetailList == null) throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        OrderDTO orderDTO =new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasters =orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
//        Page<OrderDTO> orderDTOPage =new PageImpl<>()
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
