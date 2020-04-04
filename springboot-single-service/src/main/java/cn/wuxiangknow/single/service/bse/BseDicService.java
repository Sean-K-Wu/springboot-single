package cn.wuxiangknow.single.service.bse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据字典 Service接口
 */
public interface BseDicService {

    /**
     * 根据父类或者子类的code值获取子类ID
     * @param classCode
     * @param code
     * @return
     */
    public String getIdByCode(String classCode, String code) throws Exception;

    /**
     * 根据父类或者子类的code值获取子类名称
     * @param classCode
     * @param code
     * @return
     * @throws Exception
     */
    public String getNameByCode(String classCode, String code) throws Exception;

    /**
     * 根据id获取对应条目的名称
     * @param id
     * @return
     * @throws Exception
     */
    public String getNameByPrimaryKey(String id) throws Exception;


    /**
     * 插入数据字典分类(含子类)
     * @param parent
     * @param son
     * @throws Exception
     */
    public void insert(Map<String,String> parent, LinkedHashMap<String, String> son)throws Exception;

    /**
     * 根据父类code获取子类map集合  key:id value:name
     * @param classCode
     * @return
     * @throws Exception
     */
    public Map<String,String> getSonData(String classCode)throws Exception;



    /**
     * 根据父类code以及子类名称获取子类id
     * @param pCode
     * @param sonName
     * @return
     * @throws Exception
     */
    public String getSonId(String pCode, String sonName)throws Exception;

    /**
     * 根据父类code获取子类List集合  key:id value:name
     * @param pCode
     * @return
     * @throws Exception
     */
    ArrayList<Map<String,Object>> getSonDataInList(String pCode)throws Exception;
}
