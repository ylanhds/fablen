package com.fablen.auth.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fablen.security.filter.JwtAuthenticationFilter;
import com.fablen.security.jwt.JwtUtil;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;
@MapperScan("com.fablen.auth.mapper")
@Configuration
public class AutoConfiguration {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    // 密码编码器配置
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // LDAP 配置
    @Configuration
    static class LdapConfig {
        @Value("${spring.ldap.urls}")
        private String ldapUrls;

        @Value("${spring.ldap.base}")
        private String ldapBase;

        @Value("${spring.ldap.username}")
        private String ldapUser;

        @Value("${spring.ldap.password}")
        private String ldapPassword;

        @Value("${spring.ldap.userDnPattern}")
        private String userDnPattern;

        @Bean
        public LdapContextSource contextSource() {
            LdapContextSource contextSource = new LdapContextSource();
            contextSource.setUrl(ldapUrls);
            contextSource.setBase(ldapBase);
            contextSource.setUserDn(ldapUser);
            contextSource.setPassword(ldapPassword);
            contextSource.afterPropertiesSet();
            return contextSource;
        }

        @Bean
        public LdapAuthenticator ldapAuthenticator(LdapContextSource contextSource) {
            BindAuthenticator authenticator = new BindAuthenticator(contextSource);
            authenticator.setUserDnPatterns(new String[]{userDnPattern});
            return authenticator;
        }
    }

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        return interceptor;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));
        return sessionFactory;
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
