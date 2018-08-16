package com.hk.sell.controller;

import com.hk.sell.VO.ProductInfoVO;
import com.hk.sell.VO.ProductVO;
import com.hk.sell.VO.ResultVO;
import com.hk.sell.bean.ProductCategory;
import com.hk.sell.bean.ProductInfo;
import com.hk.sell.service.CategoryService;
import com.hk.sell.service.ProductService;
import com.hk.sell.util.ResultVOUtil;
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

        //保存所有类目的列表，里面包括了商品详细信息
        List<ProductVO> productVOList =new ArrayList<>();

        //获取所有类目信息
        for(ProductCategory productCategory:productCategoryList){
            //新建一个类目VO存储类目信息
            ProductVO  productVO =new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            //每个类目里面的具体商品信息
            List<ProductInfoVO> productInfoVOList =new ArrayList<>();
            for(ProductInfo productInfo:productInfos){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO =new ProductInfoVO();
                    //拷贝对象中对应属性的值
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    //将商品信息对象保存到列表
                    productInfoVOList.add(productInfoVO);
                }
            }
            //每个类目表里面的商品列表
            productVO.setProductInfoVOList(productInfoVOList);
            //所有的类目列表
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
        //这是刚开始测试数据为空时的写法，主要为了看数据结构
//        ResultVO resultVO =new ResultVO();
//        ProductVO productVO =new ProductVO();
//        ProductInfoVO productInfoVO =new ProductInfoVO();
//        productVO.setProductInfoVOList(Arrays.asList(productInfoVO));
//        resultVO.setCode(0);
//        resultVO.setMsg("成功");
//        resultVO.setData(Arrays.asList(productVO));
//        resultVO.setData(productVOList);
//        return resultVO;
    }

}
