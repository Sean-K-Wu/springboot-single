package cn.wuxiangknow.single.service.jwt;

import cn.wuxiangknow.single.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SysUserDetailsService extends UserDetailsService, BaseService {

    /**
     * QQ 登录
     * @param openid
     * @param unionid
     * @param nickname
     * @param headImgUrl
     * @return
     * @throws Exception
     */
    String login(String openid, String unionid, String nickname, String headImgUrl) throws Exception;

    /**
     * 刷新Token
     * @param token
     * @return
     * @throws Exception
     */
    String refresh(String token) throws Exception;
}
