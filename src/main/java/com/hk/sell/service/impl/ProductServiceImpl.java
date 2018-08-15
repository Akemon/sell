package com.hk.sell.service.impl;

import com.hk.sell.bean.ProductInfo;
import com.hk.sell.dto.CartDTO;
import com.hk.sell.enums.ProductStatusEnum;
import com.hk.sell.enums.ResultEnum;
import com.hk.sell.exception.SellException;
import com.hk.sell.repository.ProductInfoRepository;
import com.hk.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author 何康
 * @date 2018/8/13 20:47
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Override
    public ProductInfo findOne(String productId) {

        return productInfoRepository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {

    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO:cartDTOList){
            ProductInfo product = productInfoRepository.findById(cartDTO.getProductId()).orElse(null);
            if(product==null) throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            Integer result =product.getProductStock() - cartDTO.getProductQuantity();
            if(result<0) throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            product.setProductStock(result);
            productInfoRepository.save(product);
        }
    }
}
