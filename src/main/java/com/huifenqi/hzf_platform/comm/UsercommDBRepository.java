package com.huifenqi.hzf_platform.comm;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import java.util.Map;
/**
 * Created by tengguodong on 2016/9/1.
 */
@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "usercommEntityManagerFactory", transactionManagerRef = "usercommTransactionManager", basePackages = {
        "com.huifenqi.usercomm.dao" })
public class UsercommDBRepository {
    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("usercommDB")
    private DataSource datasource;

    @Bean(name = "usercommEntityManagerFactory")
    @Qualifier("usercommEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean hzfEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(datasource).properties(getVendorProperties(datasource))
                .packages("com.huifenqi.usercomm.domain").persistenceUnit("usercomm").build();
    }

    @Bean(name = "usercommEntityManager")
    @Qualifier("usercommEntityManager")
    public EntityManager entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return hzfEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "usercommTransactionManager")
    @Qualifier("usercommTransactionManager")
    public PlatformTransactionManager usercommTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(hzfEntityManagerFactory(builder).getObject());
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
}
