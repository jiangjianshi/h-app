package com.huifenqi.hzf_platform.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by tengguodong on 2016/9/1.
 */
@org.springframework.context.annotation.Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "chargeEntityManagerFactory", transactionManagerRef = "chargetransactionManager", basePackages = {
        "com.huifenqi.usercomm.charge.dao" })
public class ChargeDBRepository {
    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("chargeDB")
    private DataSource datasource;

    @Bean(name = "chargeEntityManagerFactory")
    @Qualifier("chargeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean chargeEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(datasource).properties(getVendorProperties(datasource))
                .packages("com.huifenqi.usercomm.charge.domain").persistenceUnit("charge").build();
    }

    @Bean(name = "chargeEntityManager")
    @Qualifier("chargeEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return chargeEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "chargeTransactionManager")
    @Qualifier("chargeTransactionManager")
    public PlatformTransactionManager chargeTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(chargeEntityManagerFactory(builder).getObject());
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
}
