package cn.wuxiangknow.single.service.sys;

import cn.wuxiangknow.single.po.sys.SysUser;
import cn.wuxiangknow.single.service.BaseService;

public interface SysUserService extends BaseService {
    SysUser findById(Long id) throws Exception;

    SysUser findByOpenid$Unionid(String openid, String unionid) throws Exception;

    void updateByUser(SysUser sysUser)throws Exception;

    SysUser insert(String openid, String unionid, String nickname, String headImgUrl)throws Exception;
}
