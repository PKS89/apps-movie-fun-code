package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.springframework.orm.jpa.vendor.Database.MYSQL;

@Configuration
public class DbConfig {
    @Bean
    public DataSource albumsDataSource(
            @Value("${moviefun.datasources.albums.url}") String url,
            @Value("${moviefun.datasources.albums.username}") String username,
            @Value("${moviefun.datasources.albums.password}") String password
    ) {

       /* MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
        */
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setJdbcUrl(url);
        return hikariDataSource;
    }
    @Bean
    public DataSource moviesDataSource(
            @Value("${moviefun.datasources.movies.url}") String url,
            @Value("${moviefun.datasources.movies.username}") String username,
            @Value("${moviefun.datasources.movies.password}") String password
    ) {
        /*
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
        */
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setJdbcUrl(url);
        return hikariDataSource;
    }
    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapte = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapte.setDatabase(Database.MYSQL);
        hibernateJpaVendorAdapte.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        hibernateJpaVendorAdapte.setGenerateDdl(true);
        return hibernateJpaVendorAdapte;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean albumsEntityManagerFactory(@Qualifier("albumsDataSource") DataSource dataSource, HibernateJpaVendorAdapter  hibernateJpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        localContainerEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.albums");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("albums");
        return localContainerEntityManagerFactoryBean;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean moviesEntityManagerFactory(@Qualifier("moviesDataSource") DataSource dataSource,HibernateJpaVendorAdapter  hibernateJpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        localContainerEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.movies");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("movies");
        return localContainerEntityManagerFactoryBean;
    }
    @Bean
    public PlatformTransactionManager albumsTransactionManager(@Qualifier("albumsEntityManagerFactory") LocalContainerEntityManagerFactoryBean albumsEntityManagerFactory ) {
        PlatformTransactionManager platformTransactionManager = new JpaTransactionManager(albumsEntityManagerFactory.getObject());
        return platformTransactionManager;
    }
    @Bean
    public PlatformTransactionManager moviesTransactionManager(@Qualifier("moviesEntityManagerFactory") LocalContainerEntityManagerFactoryBean moviesEntityManagerFactory ) {
        PlatformTransactionManager platformTransactionManager = new JpaTransactionManager(moviesEntityManagerFactory.getObject());
        return platformTransactionManager;
    }
}
