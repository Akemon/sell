package com.hk.sell.controller;

import com.hk.sell.enums.ResultEnum;
import com.hk.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
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

/**
 * @author 何康
 * @date 2018/8/18 18:31
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    private WxMpService wxMpService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl")String returnUrl){
        //1 .配置
        wxMpService =new WxMpServiceImpl();
        WxMpInMemoryConfigStorage wxMpConfigStorage =new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId("wx7ed8085780363b9d");
        wxMpConfigStorage.setSecret("1b27c02bda115ab4e01908973c30d817");
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        //2.调用方法
        String url ="http://hksite.mynatapp.cc/sell/wechat/userInfo";
        String redirectUrl =wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        log.info("【微信网页授权】获取code,result={}",redirectUrl);
        return "redirect:"+redirectUrl;
    }
    @GetMapping("/userInfo")
    public String userInof(@RequestParam("code")String code,
                         @RequestParam("state")String returnUrl){
        WxMpOAuth2AccessToken  wxMpOAuth2AccessToken;
        try{
            wxMpOAuth2AccessToken  =wxMpService.oauth2getAccessToken(code);
        }catch(WxErrorException e){
            log.error("【微信网页授权】 {}",e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:"+returnUrl + "?openid="+openId;
    }
}
