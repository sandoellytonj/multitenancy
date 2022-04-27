package com.example.tenant.multitenancy;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.example.tenant.repository.emissor"},
        entityManagerFactoryRef = "privateEntityManager",
        transactionManagerRef = "privateTransactionManager")
public class DatabasePrivateConfiguration {

    private final PersistenceUnitManager persistenceUnitManager;
    private final DataSource dataSource;
    //private final JpaProperties jpaProperties;

    public DatabasePrivateConfiguration(ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                        DataSource dataSource
                                        ) {
        this.persistenceUnitManager = persistenceUnitManager.getIfAvailable();
        this.dataSource = dataSource;
    }

    //@Primary
    @Bean
    @ConfigurationProperties("tenant.private.jpa")
    public JpaProperties privateJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    public JpaVendorAdapter privateJpaVendorAdapter(JpaProperties privateJpaProperties) {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        //hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(privateJpaProperties.getDatabase());
        hibernateJpaVendorAdapter.setShowSql(privateJpaProperties.isShowSql());
        hibernateJpaVendorAdapter.setDatabasePlatform(privateJpaProperties.getDatabasePlatform());
        return hibernateJpaVendorAdapter;
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties privateJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = privateJpaVendorAdapter(privateJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter,
                privateJpaProperties.getProperties(), this.persistenceUnitManager);
    }

    @Bean
    public JpaTransactionManager privateTransactionManager(
            EntityManagerFactory privateEntityManager) {
        return new JpaTransactionManager(privateEntityManager);
    }

    /*@Bean
    public TenantSwitchDataSource privateDataSource() {
        return new TenantSwitchDataSource(dataSource);
    }*/

    @Bean
    public LocalContainerEntityManagerFactoryBean privateEntityManager() {

        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(privateJpaProperties());

        return builder
                //.dataSource(privateDataSource())
                .dataSource(new TenantSwitchDataSource(dataSource))
                .packages("com.example.tenant.model.emissor")
                .persistenceUnit("emissorDs")
                .build();
    }

}
