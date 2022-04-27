package com.example.tenant.multitenancy;

import com.example.tenant.model.api.Base;
import com.example.tenant.utils.DataSourceUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TenantSwitchDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(TenantSwitchDataSource.class);

    @Getter
    private ConcurrentHashMap<Object, Object> targetDataSources;

    public TenantSwitchDataSource() {
        setTargetDataSources(new ConcurrentHashMap<>());
    }

    public TenantSwitchDataSource(DataSource dataSource) {
        Map<Object, Object> dataSources = new ConcurrentHashMap<>();
        setTargetDataSources(dataSources);
        addDataSource("default", dataSource);
    }

    public void addDataSource(String emissor, DataSource ds) {
        getTargetDataSources().put(emissor, ds);
        setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = (ConcurrentHashMap<Object, Object>) targetDataSources;
        super.setTargetDataSources(targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Base base = TenantContext.get();

        if (base != null) {

            if (!base.getNome().equals("default")
                    && getTargetDataSources() != null
                    && !getTargetDataSources().containsKey(base.getNome())) {
                logger.debug("Data Source {} ainda não existe.", base.getNome());

                logger.debug("Criando a Base {}", base.getNome());

               HikariDataSource hikariDataSource = DataSourceUtil.createTenantDataSource(base);
               addDataSource(base.getNome(), hikariDataSource);

            }
            return base.getNome();
        }
        return "default";
    }
}
