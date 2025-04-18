package com.learning.config;

import com.learning.entity.FootballPlayer;
import com.learning.entity.Student;
import com.learning.utils.DirtyDataInspector;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {
    @Value("${app.datasource.url}")
    private String dbUrl;

    @Value("${app.datasource.username}")
    private String dbUser;

    @Value("${app.datasource.password}")
    private String dbPass;

    @Value("${app.datasource.driver}")
    private String dbDriver;

    @Value("${app.datasource.dialect}")
    private String hibernateDialect;

    @Value("${app.datasource.hbm2ddl}")
    private String hbm2ddlAuto;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPass);
        ds.setDriverClassName(dbDriver);
        return ds;
    }

    /*@Bean
    public SessionFactory sessionFactory() {
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, dbDriver);
        settings.put(Environment.URL, dbUrl);
        settings.put(Environment.USER, dbUser);
        settings.put(Environment.PASS, dbPass);
        settings.put(Environment.DIALECT, hibernateDialect);
        settings.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        settings.put(Environment.SHOW_SQL, "true");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(FootballPlayer.class);

        return metadataSources.buildMetadata().buildSessionFactory();
    }*/

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public DirtyDataInspector dirtyDataInspector() {
        return new DirtyDataInspector();
    }

    @Bean
    public SessionFactory sessionFactory(DirtyDataInspector dirtyDataInspector) {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                //.applySettings(settings)
                .build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Student.class);

        return metadataSources.getMetadataBuilder()
                .build()
                .getSessionFactoryBuilder()
                .applyInterceptor(dirtyDataInspector)
                .build();
    }
}
