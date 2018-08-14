package com.hk.sell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品(包含类目)
 * @author 何康
 * @date 2018/8/14 12:17
 */
@Data
public class ProductVO {

//    类目名称
    @JsonProperty("name")
    private String categoryName;

//    类目id
    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
