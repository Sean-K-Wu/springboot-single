package cn.wuxiangknow.single.dao.bse;

import cn.wuxiangknow.single.po.bse.BseDic;
import cn.wuxiangknow.single.po.bse.BseDicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BseDicMapper {
    int countByExample(BseDicExample example);

    int deleteByExample(BseDicExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BseDic record);

    int insertSelective(BseDic record);

    List<BseDic> selectByExample(BseDicExample example);

    BseDic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BseDic record, @Param("example") BseDicExample example);

    int updateByExample(@Param("record") BseDic record, @Param("example") BseDicExample example);

    int updateByPrimaryKeySelective(BseDic record);

    int updateByPrimaryKey(BseDic record);
}