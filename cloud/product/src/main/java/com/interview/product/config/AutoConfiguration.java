package com.cloud.product.config;

import com.cloud.security.filter.JwtAuthenticationFilter;
import com.cloud.security.jwt.JwtUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

/**
 * JPA持久化配置类
 * 主要职责：
 * 1. 配置实体管理器工厂(EntityManagerFactory)
 * 2. 设置JPA实现供应商(Hibernate)
 * 3. 定义实体类扫描路径
 *
 * @author zhangbaosheng
 */
@Configuration
public class AutoConfiguration {

    /**
     * 配置JPA实体管理器工厂
     *
     * @param dataSource       数据源(自动注入)
     * @param jpaVendorAdapter JPA供应商适配器(自动注入)
     * @return 配置完成的实体管理器工厂Bean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            JpaVendorAdapter jpaVendorAdapter) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        // 设置数据源(必须)
        em.setDataSource(dataSource);

        // 设置实体类扫描包路径(重要)
        em.setPackagesToScan("com.cloud.product.entity");

        // 设置JPA实现供应商(如Hibernate)
        em.setJpaVendorAdapter(jpaVendorAdapter);

        // 设置持久化单元名称(可选)
        em.setPersistenceUnitName("default");

        // 显式指定EntityManagerFactory接口(兼容Jakarta EE 9+)
        em.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);

        return em;
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil());
    }

}