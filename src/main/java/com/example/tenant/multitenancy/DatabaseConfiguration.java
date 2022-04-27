package com.example.tenant.multitenancy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("tenant.api.datasource")
    public HikariConfig apiHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public DataSource apiDataSource() {
        return new HikariDataSource(apiHikariConfig());
    }
}
