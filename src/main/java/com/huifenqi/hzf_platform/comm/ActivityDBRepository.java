package com.huifenqi.hzf_platform.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "activityEntityManagerFactory", transactionManagerRef = "activityTransactionManager", basePackages = {
		"com.huifenqi.activity.dao" })
public class ActivityDBRepository {
	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("activityDB")
	private DataSource datasource;

	@Bean(name = "activityEntityManagerFactory")
	@Qualifier("activityEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean hfzEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(datasource).properties(getVendorProperties(datasource))
				.packages("com.huifenqi.activity.domain").persistenceUnit("activity").build();
	}

	@Bean(name = "activityEntityManager")
	@Qualifier("activityEntityManager")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return hfzEntityManagerFactory(builder).getObject().createEntityManager();
	}

	@Bean(name = "activityTransactionManager")
	@Qualifier("activityTransactionManager")
	public PlatformTransactionManager activityTransactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(hfzEntityManagerFactory(builder).getObject());
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}
}
