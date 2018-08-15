package com.hk.sell.service;

import com.hk.sell.bean.ProductInfo;
import com.hk.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 何康
 * @date 2018/8/13 20:44
 */
public interface ProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询所有在架的商品列表
     * @return
     */
    List<ProductInfo> findUpAll();

    /***
     * 查询所有商品
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    /***
     * 加库存
     */
    void increaseStock(List<CartDTO> cartDTOList);

    /***
     * 减库存
     */
    void decreaseStock(List<CartDTO> cartDTOList);
}
