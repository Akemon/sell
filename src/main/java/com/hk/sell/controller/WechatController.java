package com.hk.sell.controller;

import com.hk.sell.enums.ResultEnum;
import com.hk.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

/**
 * @author 何康
 * @date 2018/8/18 18:31
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl")String returnUrl){
        //1 .配置(使用配置的方式)
        //2.调用方法
//        WxMpInMemoryConfigStorage wxMpConfigStorage =new WxMpInMemoryConfigStorage();
//        wxMpConfigStorage.setAppId("wx7ed8085780363b9d");
//        wxMpConfigStorage.setSecret("1b27c02bda115ab4e01908973c30d817");
//        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        /**
         *   URLEncoder.encode(returnUrl) :这个是外面传进来的地址
         *   url :回调的url
         *   WxConsts.OAUTH2_SCOPE_USER_INFO：获取用户的详细信息，需要用户通过验证
         */

        //回调方法，将回到以下这个url并带着code参数
        String url ="http://hksite.mynatapp.cc/sell/wechat/userInfo";
        String redirectUrl =wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】获取code,result={}",redirectUrl);
        return "redirect:"+redirectUrl;
    }

    /***
     * 回调方法，微信自动跳转到此方法并返回code值 ，通过code值获取accessToken ，最终获取openid
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code")String code,
                         @RequestParam("state")String returnUrl){
        log.info("【进入userInfo方法】returnUrl ={}",returnUrl);
        //创建的accessToken
        WxMpOAuth2AccessToken  wxMpOAuth2AccessToken =new WxMpOAuth2AccessToken();
        try{
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }catch (WxErrorException e){
            log.error("【微信网页授权】{}",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        //根据accessToken获取openid
        String openId = wxMpOAuth2AccessToken.getOpenId();

        //返回请求的url后面接上openid
        return "redirect:"+returnUrl + "?openid="+openId;
    }
}
