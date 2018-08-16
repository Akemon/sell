package com.hk.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hk.sell.bean.OrderDetail;
import com.hk.sell.dto.OrderDTO;
import com.hk.sell.enums.ResultEnum;
import com.hk.sell.exception.SellException;
import com.hk.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 何康
 * @date 2018/8/16 20:44
 */
@Slf4j
public class OrderForm2OrderDtoConverter {

    public  static OrderDTO convert(OrderForm orderForm){
        Gson gson =new Gson();

        OrderDTO orderDTO =new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList =new ArrayList<>();
        try {
            orderDetailList =gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            log.error("【对象转换】错误， string={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

}
