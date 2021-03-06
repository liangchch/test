/**
 * Project Name:spring-boot-starter-mybatis
 * File Name:DataSourceConfiguration.java
 * Package Name:com.midai.springboot.mybatis
 * Date:2016年7月28日上午9:58:36
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.chenxun.springboot.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * ClassName:DataSourceConfiguration <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年7月28日 上午9:58:36 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Configuration
@EnableConfigurationProperties(MultiJdbcProerties.class)
@AutoConfigureBefore(MybatisAutoConfiguration.class)
public class DataSourceConfiguration {

	
	private static Log log = LogFactory.getLog(DataSourceConfiguration.class);

	@Autowired
	private MultiJdbcProerties multiJdbcProerties;

	/**
	 * 
	 * dataSource:(数据库连接对象). <br/>
	 * 
	 * @author 陈勋
	 * @return
	 * @since JDK 1.7
	 */
	@Bean(initMethod="init",destroyMethod="close")
	public DataSource dataSource() {
		
		log.debug("starter config DataSource");
		log.debug("这里有一个注解");
		JdbcProperties master=multiJdbcProerties.getMaster();
		List<JdbcProperties> slavers=multiJdbcProerties.getSlaver();
		RoutingDataSource dataSource=new RoutingDataSource();	
		DataSource masterDataSource=createDataSource(master);
		dataSource.setDefaultTargetDataSource(masterDataSource);
		Map<Object,Object> slaversMap=new HashMap<Object, Object>();
		for(JdbcProperties slaver :slavers){
			DataSourceContextHolder.addKeyList(slaver.getId());
			slaversMap.put(slaver.getId(), createDataSource(slaver));
		}
		dataSource.setTargetDataSources(slaversMap);
		
		log.debug("end config DataSource");	
		return dataSource;

	}
	
	private DataSource createDataSource(JdbcProperties jdbcProperties){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(jdbcProperties.getUrl());
		dataSource.setUsername(jdbcProperties.getUsername());
		dataSource.setPassword(jdbcProperties.getPassword());
		// 连接池属性
		dataSource.setInitialSize(jdbcProperties.getInitialSize());
		dataSource.setMaxActive(jdbcProperties.getMaxActive());
		dataSource.setMinIdle(jdbcProperties.getMinIdle());
		dataSource.setMaxWait(jdbcProperties.getMaxWait());
		dataSource.setPoolPreparedStatements(jdbcProperties.isPoolPreparedStatements());
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(jdbcProperties.getMaxPoolPreparedStatementPerConnectionSize());
		// 测试属性
		dataSource.setValidationQuery(jdbcProperties.getValidationQuery());
		dataSource.setTestOnBorrow(jdbcProperties.isTestOnBorrow());
		dataSource.setTestOnReturn(jdbcProperties.isTestOnReturn());
		dataSource.setTestWhileIdle(jdbcProperties.isTestWhileIdle());
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		dataSource.setTimeBetweenEvictionRunsMillis(jdbcProperties.getTimeBetweenEvictionRunsMillis());
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		dataSource.setMinEvictableIdleTimeMillis(jdbcProperties.getMinEvictableIdleTimeMillis());
		// 打开removeAbandoned功能
		dataSource.setRemoveAbandoned(jdbcProperties.isRemoveAbandoned());
		// 1800秒，也就是30分钟
		dataSource.setRemoveAbandonedTimeout(jdbcProperties.getRemoveAbandonedTimeout());
		// 关闭abanded连接时输出错误日志
		dataSource.setLogAbandoned(jdbcProperties.isLogAbandoned());
		// 监控数据库
//		try {
//			dataSource.setFilters("mergeStat");
//		} catch (SQLException e) {
//			log.error(e.getMessage());
//		}
		
		return dataSource; 
	}

	

}
