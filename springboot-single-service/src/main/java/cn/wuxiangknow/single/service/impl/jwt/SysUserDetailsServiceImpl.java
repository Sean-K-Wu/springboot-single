package cn.wuxiangknow.single.service.impl.jwt;


import cn.wuxiangknow.single.common.constant.DicConstant;
import cn.wuxiangknow.single.common.constant.HttpCodeEnum;
import cn.wuxiangknow.single.common.constant.RegConstant;
import cn.wuxiangknow.single.common.dto.jwt.JwtUser;
import cn.wuxiangknow.single.common.dto.jwt.JwtUserFactory;
import cn.wuxiangknow.single.common.exception.BusinessException;
import cn.wuxiangknow.single.common.jwt.JwtTokenManager;
import cn.wuxiangknow.single.po.sys.SysUser;
import cn.wuxiangknow.single.service.bse.BseDicService;
import cn.wuxiangknow.single.service.impl.BaseServiceImpl;
import cn.wuxiangknow.single.service.jwt.SysUserDetailsService;
import cn.wuxiangknow.single.service.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Descirption jwt相关service用于验证用户
 * @Author WuXiang
 * @Date 2018/6/19/
 */
@Service
@Slf4j
public class SysUserDetailsServiceImpl extends BaseServiceImpl implements UserDetailsService , SysUserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtTokenManager jwtTokenManager;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            if(Strings.isNotBlank(userId) && userId.matches(RegConstant.INTEGER_REGEX)){
                SysUser user = sysUserService.findById(Long.parseLong(userId));
                if (user == null) {
                    throw new UsernameNotFoundException(String.format("No user found with userId '%s'.", userId));
                } else {
                    return JwtUserFactory.create(String.valueOf(user.getId()),user.getUsername(),user.getPassword(),null,user.getLastLoginTime(),user.getLastPasswordResetTime());
                }
            }else{
                throw new UsernameNotFoundException("jwt illegal userId:"+userId);
            }

        } catch (Exception e) {
            log.error("jwt验证过程根据用户名获取用户出错",e);
        }
        throw new UsernameNotFoundException("jwt login something unexpected happened");
    }

    @Override
    @Transactional
    public String login(String openid, String unionid, String nickname, String headImgUrl) throws Exception {
        //校验参数
        checkQQLoginParams(openid,unionid,nickname,headImgUrl);
        //是否注册过
        SysUser sysUser = sysUserService.findByOpenid$Unionid(openid,unionid);
        if(sysUser == null){
            //注册
            sysUser = sysUserService.insert(openid,unionid,nickname,headImgUrl);
        }else{
            //更新登录时间等
            if(!nickname.equals(sysUser.getNickname())){
                sysUser.setNickname(nickname);
            }
            if(!headImgUrl.equals(sysUser.getHeadImgUrl())){
                sysUser.setHeadImgUrl(headImgUrl);
            }
            Date now = new Date();
            sysUser.setLastLoginTime(now);
            sysUserService.updateByUser(sysUser);
        }
        return jwtTokenManager.generateToken(String.valueOf(sysUser.getId()));
    }

    @Override
    public String refresh(String token) throws Exception {
        String userId = jwtTokenManager.getUserIdFromToken(token);
        JwtUser jwtUser = (JwtUser) loadUserByUsername(userId);
        if (jwtTokenManager.validateToken(token, userId,jwtUser.getLastLoginDate(),jwtUser.getLastPasswordResetDate())){
            return jwtTokenManager.refreshToken(token);
        }
        return null;
    }

    private void checkQQLoginParams(String openid, String unionid, String nickname, String headImgUrl) {
        if(Strings.isBlank(openid) || Strings.isBlank(unionid) || Strings.isBlank(nickname) || Strings.isBlank(headImgUrl)){
            throw new BusinessException(HttpCodeEnum.GL00000100);
        }
    }
}
