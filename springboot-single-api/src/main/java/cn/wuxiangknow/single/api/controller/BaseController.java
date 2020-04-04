package cn.wuxiangknow.single.api.controller;


import cn.wuxiangknow.single.common.constant.HttpCodeEnum;
import cn.wuxiangknow.single.common.dto.jwt.JwtUser;
import cn.wuxiangknow.single.common.exception.BusinessException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Descirption controller基础方法
 * @Author WuXiang
 */
public class BaseController {

    public static  final  String SUCCESS ="success";
    public static  final  String FAILURE ="false";


    /**
     * 获取当前登陆的用户
     * @return
     */
    public JwtUser getJwtUser() throws Exception{
        if(SecurityContextHolder.getContext().getAuthentication() == null){//未登录
            throw new BusinessException(HttpCodeEnum.GL00000401);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (JwtUser) authentication.getPrincipal();
    }

    public String getCurVersion(HttpServletRequest request) throws Exception{
        String curVersion = request.getHeader("curVersion");
        if(Strings.isBlank(curVersion)){
            throw new BusinessException(HttpCodeEnum.GL00000100);
        }
        return curVersion;
    }
}
