package com.aximly.electricbug.servicedetails.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CloudDataSourceConfig {

    @Value("${cloud.datasource.url}")
    private String url;

    @Value("${cloud.datasource.username}")
    private String username;

    @Value("${cloud.datasource.password}")
    private String password;

    @Value("${cloud.datasource.driver-class-name}")
    private String driverClassName;

    @Bean(name = "cloudDataSource")
    public DataSource cloudDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(2);
        config.setMinimumIdle(0);
        config.setConnectionTimeout(5000);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(270000);
        config.setPoolName("cloud-pool");
        config.setInitializationFailTimeout(-1);
        return new HikariDataSource(config);
    }
}