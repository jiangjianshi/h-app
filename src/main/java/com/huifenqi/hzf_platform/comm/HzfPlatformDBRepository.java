package com.huifenqi.hzf_platform.comm;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "hzfPlatformEntityManagerFactory", transactionManagerRef = "hzfPlatformTransactionManager", basePackages = {
		"com.huifenqi.hzf_platform.dao.repository.company",
		"com.huifenqi.hzf_platform.dao.repository.house",
		"com.huifenqi.hzf_platform.dao.repository.location",
		"com.huifenqi.hzf_platform.dao.repository.third",
		"com.huifenqi.hzf_platform.dao.repository.platform"})
public class HzfPlatformDBRepository {

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("hzfPlatformDB")
	private DataSource datasource;

	@Bean(name = "hzfPlatformEntityManagerFactory")
	@Qualifier("hzfPlatformEntityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean hzfPlatformEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(datasource).properties(getVendorProperties(datasource))
				.packages(
						"com.huifenqi.hzf_platform.context.entity.house",
						"com.huifenqi.hzf_platform.context.entity.location",
						"com.huifenqi.hzf_platform.context.entity.third",
						"com.huifenqi.hzf_platform.context.entity.saas",
						"com.huifenqi.hzf_platform.context.entity.phone",
						"com.huifenqi.hzf_platform.context.entity.platform"
						//"com.huifenqi.hzf_platform.context.entity.house.solr"
				).persistenceUnit("hzf_platform").build();
	}

	@Bean(name = "hzfPlatformmEntityManager")
	@Qualifier("hzfPlatformmEntityManager")
	@Primary
	public EntityManager entityManagerFactory(EntityManagerFactoryBuilder builder) {
		return hzfPlatformEntityManagerFactory(builder).getObject().createEntityManager();
	}

	@Bean(name = "hzfPlatformTransactionManager")
	@Qualifier("hzfPlatformTransactionManager")
	@Primary
	public PlatformTransactionManager hzfPlatformTransactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(hzfPlatformEntityManagerFactory(builder).getObject());
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

}
