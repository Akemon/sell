package com.hk.sell.controller;

import com.hk.sell.VO.ProductInfoVO;
import com.hk.sell.VO.ProductVO;
import com.hk.sell.VO.ResultVO;
import com.hk.sell.bean.ProductCategory;
import com.hk.sell.bean.ProductInfo;
import com.hk.sell.service.CategoryService;
import com.hk.sell.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 何康
 * @date 2018/8/14 12:08
 */
@RestController
@RequestMapping(value = "/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    public ResultVO list(){
        //1.查询所有的上架商品
        List<ProductInfo> productInfos =productService.findUpAll();
        //2.查询类目 (一次性查询)
        List<Integer> categoryList = new ArrayList<Integer>();
        //传统方法
        for(ProductInfo productInfo: productInfos){
            categoryList.add(productInfo.getCategoryType());
        }
        List<ProductCategory> productCategoryList =categoryService.findByCategoryTypeIn(categoryList);
        //3.数据拼装
        List<ProductVO> productVOList =new ArrayList<>();
        for(ProductCategory productCategory:productCategoryList){
            ProductVO  productVO =new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList =new ArrayList<>();
            for(ProductInfo productInfo:productInfos){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO =new ProductInfoVO();
                    //拷贝对象中对应属性的值
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }

            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        ResultVO resultVO =new ResultVO();
//        ProductVO productVO =new ProductVO();
//        ProductInfoVO productInfoVO =new ProductInfoVO();
//        productVO.setProductInfoVOList(Arrays.asList(productInfoVO));
        resultVO.setCode(0);
        resultVO.setMsg("成功");
//        resultVO.setData(Arrays.asList(productVO));
        resultVO.setData(productVOList);
        return resultVO;
    }

}
