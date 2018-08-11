package com.hk.sell;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 何康
 * @date 2018/8/11 19:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//使用lombok的的日志注解方式
@Slf4j
public class LoggerTest {
    //常用的logger生成方式
//    private static final Logger logger =LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test1(){
        //系统默认级别为info ，在info级别之下（debug）不会打印出来
        //系统级别可以从org.slf4j.event下的Level这个类去查看
//        logger.info("info....");
//        logger.debug("debug...");
//        logger.error("error...");

        //使用注解后的方式，如果idea没有安装lombok插件会报红，但是不影响执行，建议安装
        String name ="root";
        String password ="123456";
        log.info("name: {}  password: {}",name,password);
        log.debug("debug...");
        log.error("error...");
        log.error("error...");
        log.error("error...");
        log.error("error...");
        log.warn("warn...");
    }

}
