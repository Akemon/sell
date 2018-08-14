package com.hk.sell.util;

import com.hk.sell.VO.ResultVO;

/**
 * @author 何康
 * @date 2018/8/14 19:11
 */
public class ResultVOUtil {

    public static ResultVO success(Object object){
        ResultVO resultVO =new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success(){
        return success(null);
    }

    public static ResultVO error(Integer code,String msg){
        ResultVO resultVO =new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
