package com.wf.imaotai.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * @author pcc
 * 这是分页插件PageHelper的配置信息
 * 使用PageHelper的原因是MP的分页对于多表分页查询和自定义查询的分页支持不够优秀
 */
@Configuration
public class PageHelperConfig {

    /**
     * 可能存在多个连接工厂，是允许这么注入的
     */
    @Resource
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void initConfig(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 设置数据源方言，使用mysql
        properties.setProperty("helperDialect","mysql");

        pageInterceptor.setProperties(properties);
        sqlSessionFactoryList.forEach(factory ->factory.getConfiguration().addInterceptor(pageInterceptor));
    }
}