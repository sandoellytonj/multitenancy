package com.example.tenant.multitenancy;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.ObjectProvider;
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
        basePackages = {"com.example.tenant.repository.api"},
        entityManagerFactoryRef = "apiEntityManager",
        transactionManagerRef = "apiTransactionManager")
public class DatabaseApiConfiguration {

    private final PersistenceUnitManager persistenceUnitManager;
    private final DataSource dataSource;
    //private final JpaProperties jpaProperties;

    public DatabaseApiConfiguration(ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                    DataSource dataSource
                                    ) {
        this.persistenceUnitManager = persistenceUnitManager.getIfAvailable();
        this.dataSource = dataSource;
    }

    @Primary
    @Bean
    @ConfigurationProperties("tenant.api.jpa")
    public JpaProperties apiJpaProperties() {
        return new JpaProperties();
    }

    @Primary
    @Bean
    public JpaVendorAdapter apiJpaVendorAdapter(JpaProperties apiJpaProperties) {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        //hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(apiJpaProperties.getDatabase());
        hibernateJpaVendorAdapter.setShowSql(apiJpaProperties.isShowSql());
        hibernateJpaVendorAdapter.setDatabasePlatform(apiJpaProperties.getDatabasePlatform());
        return hibernateJpaVendorAdapter;
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties apiJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = apiJpaVendorAdapter(apiJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter,
                apiJpaProperties.getProperties(), this.persistenceUnitManager);
    }

    @Bean
    @Primary
    public JpaTransactionManager apiTransactionManager(
            EntityManagerFactory apiEntityManager) {
        return new JpaTransactionManager(apiEntityManager);
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean apiEntityManager() {

        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(apiJpaProperties());

        return builder
                .dataSource(dataSource)
                .packages("com.example.tenant.model.api")
                .persistenceUnit("apiDs")
                .build();
    }
}
