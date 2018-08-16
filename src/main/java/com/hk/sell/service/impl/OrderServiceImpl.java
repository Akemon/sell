package com.hk.sell.service.impl;

import com.hk.sell.bean.OrderDetail;
import com.hk.sell.bean.OrderMaster;
import com.hk.sell.bean.ProductInfo;
import com.hk.sell.converter.OrderMaster2OrderDTOConverter;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
@Slf4j
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
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
//        orderMaster.setOrderId(orderId);
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
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasters.getContent());
        Page<OrderDTO> orderDTOPage =new PageImpl<>(orderDTOList,pageable,orderMasters.getTotalElements());
        return orderDTOPage;
    }


    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        //判断订单是否存在
        //TODO

        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERRER);
        }
        //修改订单状态
        OrderMaster orderMaster =new OrderMaster();
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster orderMasterUpdate =orderMasterRepository.save(orderMaster);
        if(orderMasterUpdate == null){
            log.error("【取消订单】更新失败");
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返回库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】订单无商品详情");
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList =orderDTO.getOrderDetailList().stream().map(e ->
                    new CartDTO(e.getProductId(),e.getProductQuantity())
                ).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
        //如果已支付，需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }
        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单是否存在
        //TODO

        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】订单状态不正确,orderId={} orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERRER);
        }
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //修改订单状态
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if(result == null){
            log.error("【完结订单】更新失败");
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付】订单状态不正确,orderId={} orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERRER);
        }

        //判断支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付】订单支付状态不正确");
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //修改订单状态
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if(result == null){
            log.error("【订单支付】更新支付状态失败");
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }
}
