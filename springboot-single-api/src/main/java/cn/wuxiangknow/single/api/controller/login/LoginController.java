package cn.wuxiangknow.single.api.controller.login;


import cn.wuxiangknow.single.api.dto.login.LoginQQReqDto;
import cn.wuxiangknow.single.common.constant.HttpCodeEnum;
import cn.wuxiangknow.single.common.dto.HttpResponseWrapper;
import cn.wuxiangknow.single.service.jwt.SysUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Descirption 登录Controller
 * @Author WuXiang
 */
@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private SysUserDetailsService sysUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * 获取token
     * @param loginQQReqDto
     * @return HttpResponseWrapper
     * @throws AuthenticationException
     */

    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    public HttpResponseWrapper login(@RequestBody LoginQQReqDto loginQQReqDto) throws Exception {
        try {
            String token = sysUserDetailsService.login(loginQQReqDto.getOpenid(),loginQQReqDto.getUnionid(),loginQQReqDto.getNickname(),loginQQReqDto.getHeadImgUrl());
            // Return the token
            return  HttpResponseWrapper.success(token);
        } catch (Exception e) {
            if(e instanceof AuthenticationException){
                return HttpResponseWrapper.error(HttpCodeEnum.GL00000100);
            }
            throw e;
        }
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public HttpResponseWrapper refreshAndGetAuthenticationToken(HttpServletRequest request) throws Exception{
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshedToken = sysUserDetailsService.refresh(token);
        if(refreshedToken == null) {
            return HttpResponseWrapper.error(HttpCodeEnum.GL00000401);
        } else {
            return HttpResponseWrapper.success(refreshedToken);
        }
    }

}
