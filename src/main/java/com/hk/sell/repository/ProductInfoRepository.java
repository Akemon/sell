package com.hk.sell.repository;

import com.hk.sell.bean.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 何康
 * @date 2018/8/13 20:02
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    //查询上架的商品
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
