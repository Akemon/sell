package com.hk.sell.util;

import java.util.Random;

/**
 * 生成随机数的工具类
 * @author 何康
 * @date 2018/8/14 21:19
 */
public class KeyUtil {
    /***
     * 生成唯一的主键
     * 格式：时间+随机数
     * @return
     */
    public static synchronized  String genUniqueKey(){
        Random random =new Random();
        //生成6位随机数
        Integer number =random.nextInt(900000)+100000;
        return System.currentTimeMillis()+String.valueOf(number);
    }
}
