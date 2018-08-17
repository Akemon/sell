package com.hk.sell.controller;

import com.hk.sell.VO.ResultVO;
import com.hk.sell.converter.OrderForm2OrderDtoConverter;
import com.hk.sell.dto.OrderDTO;
import com.hk.sell.enums.OrderStatusEnum;
import com.hk.sell.enums.ResultEnum;
import com.hk.sell.exception.SellException;
import com.hk.sell.form.OrderForm;
import com.hk.sell.service.BuyerService;
import com.hk.sell.service.OrderService;
import com.hk.sell.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 何康
 * @date 2018/8/16 20:30
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    /***
     *  创建订单
     * @param orderForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public ResultVO<Map<String,String>>  create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("【创建订单】表单验证失败, orderFomr={}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderForm2OrderDtoConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO result =orderService.create(orderDTO);
        Map<String,String> map =new HashMap<>();
        map.put("orderId",result.getOrderId());
        return ResultVOUtil.success(map);
    }

    /***
     * 获取订单列表
     * @param openid
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid")String openid,
                                         @RequestParam(value = "page",defaultValue = "0")Integer page,
                                         @RequestParam(value = "size",defaultValue = "10")Integer size){
        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表 】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest request =new PageRequest(page,size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);
        List<OrderDTO> orderDTOList =orderDTOPage.getContent();
        return ResultVOUtil.success(orderDTOList);
    }


    //订单详情
    @PostMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid")String openid,
                                     @RequestParam("orderId")String orderId){

//        OrderDTO order = orderService.findOne(orderId);
//        return ResultVOUtil.success(order);
        OrderDTO orderDTO =buyerService.findOrderOne(openid,orderId);
        return ResultVOUtil.success(orderDTO);
    }
    //取消订单

    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid")String openid,
                           @RequestParam("orderId")String orderId){
//        OrderDTO orderDTO =orderService.findOne(orderId);
//        OrderDTO result =orderService.cancel(orderDTO);
//        return ResultVOUtil.success(result);
        OrderDTO orderDTO =buyerService.cancelOrder(openid,orderId);
        return ResultVOUtil.success();
    }
}
