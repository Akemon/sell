package com.hk.sell.repository;

import com.hk.sell.bean.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 何康
 * @date 2018/8/13 14:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest(){
       ProductCategory productCategory = productCategoryRepository.findById(1).orElse(null);
       System.out.println(productCategory);
    }

    @Test
    //该注解的Test下使用完全回滚，数据不会写入到数据库，保证数据整洁
    @Transactional
    public void saveTest(){
        ProductCategory productCategory =new ProductCategory();
        productCategory.setCategoryName("谁都爱");
        productCategory.setCategoryType(17);
        ProductCategory result =productCategoryRepository.save(productCategory);
        Assert.assertNotNull(result);
    }

    @Test
    public void updateTest(){
        ProductCategory productCategory = productCategoryRepository.findById(3).orElse(null);
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(11);
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void  categoryListTes(){
        List<Integer> list= Arrays.asList(1,2,3);
        List typeList =   productCategoryRepository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,typeList.size());
    }


}