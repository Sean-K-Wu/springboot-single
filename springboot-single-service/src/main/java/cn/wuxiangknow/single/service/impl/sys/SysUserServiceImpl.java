package cn.wuxiangknow.single.service.impl.sys;

import cn.wuxiangknow.single.common.constant.DicConstant;
import cn.wuxiangknow.single.dao.sys.SysUserMapper;
import cn.wuxiangknow.single.po.sys.SysUser;
import cn.wuxiangknow.single.po.sys.SysUserExample;
import cn.wuxiangknow.single.service.bse.BseDicService;
import cn.wuxiangknow.single.service.sys.SysUserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private BseDicService bseDicService;

    @Override
    public SysUser findById(Long id) throws Exception {
        if(id != null){
            return sysUserMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public SysUser findByOpenid$Unionid(String openid, String unionid) throws Exception {
        if(Strings.isNotBlank(openid) || Strings.isNotBlank(unionid)){
            SysUserExample example = new SysUserExample();
            SysUserExample.Criteria criteria = example.createCriteria();
            if(Strings.isNotBlank(openid)){
                criteria.andOpenidEqualTo(openid);
            }
            if(Strings.isNotBlank(unionid)){
                criteria.andUnionidEqualTo(unionid);
            }
            List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
            if(!sysUsers.isEmpty()){
                return sysUsers.get(0);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void updateByUser(SysUser sysUser) throws Exception {
        sysUser.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    @Transactional
    public SysUser insert(String openid, String unionid, String nickname, String headImgUrl) throws Exception {
        if(Strings.isNotBlank(openid) && Strings.isNotBlank(unionid) && Strings.isNotBlank(nickname) && Strings.isNotBlank(headImgUrl)){
            SysUser sysUser = new SysUser();
            sysUser.setOpenid(openid);
            sysUser.setUnionid(unionid);
            sysUser.setNickname(nickname);
            sysUser.setHeadImgUrl(headImgUrl);
            Date now = new Date();
            sysUser.setType(bseDicService.getIdByCode(DicConstant.USER_TYPE_P_CODE,DicConstant.USER_TYPE_S_QQ_CODE));
            sysUser.setLastLoginTime(now);
            sysUser.setCreateTime(now);
            sysUser.setUpdateTime(now);
            sysUserMapper.insertSelective(sysUser);
            return sysUser;
        }
        return null;
    }
}
