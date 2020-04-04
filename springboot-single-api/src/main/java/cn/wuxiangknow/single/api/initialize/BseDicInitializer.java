package cn.wuxiangknow.single.api.initialize;


import cn.wuxiangknow.single.common.constant.DicConstant;
import cn.wuxiangknow.single.service.bse.BseDicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Descirption 数据字典初始化
 * @Author WuXiang
 */
@Component
@Slf4j
@Order(1)
public class BseDicInitializer implements CommandLineRunner {
    @Autowired
    private BseDicService bseDicService;


    @Override
    public void run(String... strings) throws Exception {
        initBseDic();
        log.info("BSE_DIC INITIALIZER FINISHED...");
    }

    private void initBseDic() throws Exception{
        HashMap<String, String> parent = new LinkedHashMap<>();
        LinkedHashMap<String, String> son = new LinkedHashMap<>();
        //插入
        parent.put(DicConstant.DATA_YN_P_CODE,"数据是否可用");
        son.put(DicConstant.DATA_YN_S_Y_CODE,"可用");
        son.put(DicConstant.DATA_YN_S_N_CODE,"不可用");
        bseDicService.insert(parent,son);
        clear(parent,son);
        //插入性别
        parent.put(DicConstant.SEX_P_CODE,"性别");
        son.put(DicConstant.SEX_S_M_CODE,"男");
        son.put(DicConstant.SEX_S_F_CODE,"女");
        bseDicService.insert(parent,son);
        clear(parent,son);
        //插入用户类型
        parent.put(DicConstant.USER_TYPE_P_CODE,"用户类型");
        son.put(DicConstant.USER_TYPE_S_QQ_CODE,"QQ用户");
        bseDicService.insert(parent,son);
        clear(parent,son);
    }

    private void clear(Map<String,String> parent, LinkedHashMap<String, String> son) {
        parent.clear();
        son.clear();
    }
}
