package com.cloud.auth.config;

import com.fablen.security.filter.JwtAuthenticationFilter;
import com.fablen.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

import javax.sql.DataSource;

@Configuration
public class AutoConfiguration {

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

    // JPA 配置
    @Configuration
    static class JpaConfig {
        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(
                DataSource dataSource,
                JpaVendorAdapter jpaVendorAdapter) {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource);
            em.setPackagesToScan("com.cloud.auth.entity");
            em.setJpaVendorAdapter(jpaVendorAdapter);
            em.setPersistenceUnitName("default");

            // 关键设置
            em.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);
            return em;
        }
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
