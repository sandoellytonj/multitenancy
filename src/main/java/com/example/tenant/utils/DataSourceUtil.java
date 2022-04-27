package com.example.tenant.utils;

import com.example.tenant.model.api.Base;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceUtil {

    public static HikariDataSource createTenantDataSource(Base base) {
        HikariDataSource customDataSource = new HikariDataSource();
        customDataSource.setJdbcUrl(base.getUrl());
        customDataSource.setUsername(base.getUsuario());
        customDataSource.setPassword(base.getSenha());
        customDataSource.setAutoCommit(false);
        customDataSource.setPoolName(base.getNome());
        customDataSource.setConnectionTimeout(60000);
        customDataSource.setIdleTimeout(30000);
        customDataSource.setConnectionTestQuery("SELECT 1 FROM DUAL");
        customDataSource.setMaximumPoolSize(20);
        customDataSource.setMinimumIdle(5);
        return customDataSource;
    }

}
