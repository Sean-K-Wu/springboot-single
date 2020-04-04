package cn.wuxiangknow.single.api;


import org.apache.catalina.filters.RequestDumperFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @Descirption 启动项目类
 * @Author WuXiang
 * @Date 2018/6/4/
 */
@SpringBootApplication
@MapperScan("cn.wuxiangknow.mylife.dao")
@EnableCaching
@EnableTransactionManagement
public class ApiApplication {
    /**
     * @Description 启动项目
     * @Author WuXiang
     * @Date 2018/6/4/ 14:24
     * @param args
     * @return void
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    /**
     * 为测试环境添加相关的 Request Dumper information，便于调试
     * @return
     */
    @Profile({"dev","test"})
    @Bean
    RequestDumperFilter requestDumperFilter() {

        return new RequestDumperFilter();
    }

}
