package cn.wuxiangknow.single.service.impl.bse;

import cn.wuxiangknow.single.common.constant.RegConstant;
import cn.wuxiangknow.single.dao.bse.BseDicMapper;
import cn.wuxiangknow.single.po.bse.BseDic;
import cn.wuxiangknow.single.po.bse.BseDicExample;
import cn.wuxiangknow.single.service.bse.BseDicService;
import cn.wuxiangknow.single.service.impl.BaseServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BseDicServiceImpl extends BaseServiceImpl implements BseDicService {
    @Autowired
    private BseDicMapper bseDicMapper;

    /**
     * 缓存注解说明
     * 根据code获取id  缓存中key分隔符: '_'
     * 根据code获取名称 缓存中key分隔符: '-'
     * 根据id获取记录  缓存中key前缀: '#'
     * 根据父类code获取子类map 缓存中key前缀: '$'
     */

    @Override
    @Cacheable(value = "dicCache", key ="#classCode+'_'+#code" ,unless = "#result==null")
    public String getIdByCode(String classCode, String code) throws Exception {
        BseDicExample bseDicExample = new BseDicExample();
        BseDicExample.Criteria criteria = bseDicExample.createCriteria();
        criteria.andCodeEqualTo(classCode);
        List<BseDic> bseDics = bseDicMapper.selectByExample(bseDicExample);
        if(bseDics.size()>0){
            BseDic bseDic = bseDics.get(0);
            bseDicExample.clear();
            criteria = bseDicExample.createCriteria();
            criteria.andParentIdEqualTo(bseDic.getId());
            criteria.andCodeEqualTo(code);
            bseDics = bseDicMapper.selectByExample(bseDicExample);
            if(bseDics.size()>0){
                return String.valueOf(bseDics.get(0).getId());
            }
        }
        return null;
    }


    @Override
    @Cacheable(value = "dicCache", key ="#classCode+'-'+#code" ,unless = "#result==null")
    public String getNameByCode(String classCode, String code) throws Exception {
        BseDicExample bseDicExample = new BseDicExample();
        BseDicExample.Criteria criteria = bseDicExample.createCriteria();
        criteria.andCodeEqualTo(classCode);
        List<BseDic> bseDics = bseDicMapper.selectByExample(bseDicExample);
        if(bseDics.size()>0){
            BseDic bseDic = bseDics.get(0);
            bseDicExample.clear();
            criteria = bseDicExample.createCriteria();
            criteria.andParentIdEqualTo(bseDic.getId());
            criteria.andCodeEqualTo(code);
            bseDics = bseDicMapper.selectByExample(bseDicExample);
            if(bseDics.size()>0){
                return bseDics.get(0).getName();
            }
        }
        return null;
    }

    @Override
    @Cacheable(value = "dicCache", key ="'#'+#id" ,unless = "#result==null")
    public String getNameByPrimaryKey(String id) throws Exception {
        if(Strings.isNotBlank(id)
                && id.matches(RegConstant.NUMBER_REGEX)
        ){
            BseDic bseDic = bseDicMapper.selectByPrimaryKey(Long.parseLong(id));
            if(bseDic!=null){
                return bseDic.getName();
            }
        }
        return null;
    }


    @Override
    @Transactional
    public void insert(Map<String,String> parent, LinkedHashMap<String, String> son) throws Exception {
        if(parent.size()==1
                && son.size()>0
        ){
            Set<String> strings = parent.keySet();
            String parentCode =null;
            for (String string : strings) {
                parentCode = string;
            }
            String[] sonCodes = new String[son.size()];
            son.keySet().toArray(sonCodes);
            //查询该分类是否存在
            BseDicExample bseDicExample = new BseDicExample();
            BseDicExample.Criteria criteria = bseDicExample.createCriteria();
            criteria.andCodeEqualTo(parentCode);
            List<BseDic> bseDics = bseDicMapper.selectByExample(bseDicExample);
            Long parentId;
            if(bseDics.size()>0){//存在
                parentId = bseDics.get(0).getId();
            }else{
                //插入
                bseDicExample.clear();
                criteria = bseDicExample.createCriteria();
                criteria.andParentIdIsNull();
                bseDicExample.setOrderByClause("weight desc");
                bseDics = bseDicMapper.selectByExample(bseDicExample);
                Integer weight = 1;
                if(bseDics.size()>0){
                    weight = bseDics.get(0).getWeight()+1;
                }
                BseDic bseDic = new BseDic();
                bseDic.setWeight(weight);
                bseDic.setName(parent.get(parentCode));
                bseDic.setCode(parentCode);
                bseDicMapper.insertSelective(bseDic);
                parentId = bseDic.getId();
            }
            //插入子类数据
            //先查子类最高weight
            bseDicExample.clear();
            criteria = bseDicExample.createCriteria();
            criteria.andParentIdEqualTo(parentId);
            bseDicExample.setOrderByClause("weight desc");
            bseDics = bseDicMapper.selectByExample(bseDicExample);
            Integer weight = 0;
            if(bseDics.size()>0){
                weight = bseDics.get(0).getWeight();
            }
            for (String sonCode : sonCodes) {
                bseDicExample.clear();
                criteria = bseDicExample.createCriteria();
                criteria.andCodeEqualTo(sonCode);
                bseDics = bseDicMapper.selectByExample(bseDicExample);
                if(bseDics.size()>0){
                    continue;//下一次
                }
                weight++;
                BseDic bseDic = new BseDic();
                bseDic.setCode(sonCode);
                bseDic.setName(son.get(sonCode));
                bseDic.setWeight(weight);
                bseDic.setParentId(parentId);
                bseDicMapper.insertSelective(bseDic);
            }
        }
    }

    @Override
    @Cacheable(value = "dicCache", key ="'$'+#classCode" ,unless = "#result==null")
    public Map<String, String> getSonData(String classCode) throws Exception {
        if(Strings.isNotBlank(classCode)){
            BseDicExample bseDicExample = new BseDicExample();
            BseDicExample.Criteria criteria = bseDicExample.createCriteria();
            criteria.andCodeEqualTo(classCode);
            List<BseDic> bseDics = bseDicMapper.selectByExample(bseDicExample);
            if(bseDics.size()>0) {
                BseDic bseDic = bseDics.get(0);
                bseDicExample.clear();
                criteria = bseDicExample.createCriteria();
                criteria.andParentIdEqualTo(bseDic.getId());
                bseDics = bseDicMapper.selectByExample(bseDicExample);
                if(bseDics.size()>0){
                    HashMap<String, String> map = new LinkedHashMap<>(bseDics.size());
                    for (BseDic dic : bseDics) {
                        map.put(String.valueOf(dic.getId()),dic.getName());
                    }
                    return map;
                }
            }
        }
        return null;
    }




    @Override
    public String getSonId(String pCode, String sonName) throws Exception {
        String sonId =null;
        if(Strings.isNotBlank(sonName)){
            Map<String, String> sonData = getSonData(pCode);
            Set<Map.Entry<String, String>> entries = sonData.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if(entry.getValue().equals(sonName)){
                    sonId = entry.getKey();
                    break;
                }
            }
        }

        return sonId;
    }

    @Override
    public ArrayList<Map<String, Object>> getSonDataInList(String pCode) throws Exception {
        Map<String, String> dataMap = getSonData(pCode);
        Set<Map.Entry<String, String>> entries = dataMap.entrySet();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> data;
        for (Map.Entry<String, String> entry : entries) {
            data = new HashMap<>();
            data.put("key", entry.getKey());
            data.put("value", entry.getValue());
            list.add(data);
        }
        return list;
    }
}
