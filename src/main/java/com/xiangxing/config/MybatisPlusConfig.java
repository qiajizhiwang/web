package com.xiangxing.config;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

@Configuration
// 扫描dao或者是Mapper接口
@MapperScan("com.xiangxing.mapper")
public class MybatisPlusConfig {
	/**
	 * mybatis-plus 分页插件
	 */

	@Bean
	public Interceptor getInterceptor() {
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		p.setProperty("dialect", "mysql");
		pageHelper.setProperties(p);
		return pageHelper;
	}

}
