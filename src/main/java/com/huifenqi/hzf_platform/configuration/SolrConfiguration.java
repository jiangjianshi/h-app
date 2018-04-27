/** 
* Project Name: hzf_platform_project 
* File Name: SolrConfiguration.java 
* Package Name: com.huifenqi.hzf_platform.configuration 
* Date: 2016年5月12日下午1:53:50 
* Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
* 
*/
package com.huifenqi.hzf_platform.configuration;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.SolrServerFactory;
import org.springframework.data.solr.server.support.MulticoreSolrServerFactory;

import com.huifenqi.hzf_platform.context.Constants;

/**
 * ClassName: SolrConfiguration date: 2016年5月12日 下午1:53:50 Description:
 * 
 * @author maoxinwu
 * @version
 * @since JDK 1.8
 */
@Configuration
public class SolrConfiguration {

	private static final String PROPERTY_NAME_SOLR_SERVER_URL = "spring.data.solr.host";

	@Resource
	private Environment environment;

	@Bean
	public SolrServerFactory solrServerFactory() {
		return new MulticoreSolrServerFactory(
				new HttpSolrServer(environment.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_URL)));
	}

	@Bean
	public SolrTemplate hfqHouseTemplate() throws Exception {
		SolrTemplate solrTemplate = new SolrTemplate(solrServerFactory());
		solrTemplate.setSolrCore(Constants.SolrConstant.CORE_HOUSE);
		return solrTemplate;
	}

	@Bean
	public SolrTemplate hfqRoomTemplate() throws Exception {
		SolrTemplate solrTemplate = new SolrTemplate(solrServerFactory());
		solrTemplate.setSolrCore(Constants.SolrConstant.CORE_ROOM);
		return solrTemplate;
	}

	@Bean
	public SolrTemplate hzfHousesTemplate() throws Exception {
		SolrTemplate solrTemplate = new SolrTemplate(solrServerFactory());
		solrTemplate.setSolrCore(Constants.SolrConstant.CORE_HZF_HOUSES);
		return solrTemplate;
	}
}
