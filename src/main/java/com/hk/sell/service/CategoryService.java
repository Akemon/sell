package com.hk.sell.service;

import com.hk.sell.bean.ProductCategory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目
 * @author 何康
 * @date 2018/8/13 19:39
 */
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
