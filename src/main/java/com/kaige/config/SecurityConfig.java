package com.kaige.config;

import com.kaige.service.LoginLogService;
import com.kaige.service.impl.LoginLogServiceImpl;
import com.kaige.service.impl.UserServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 开启WebSecurity
public class SecurityConfig{

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private LoginLogService loginLogService;

    /**
     * 密码加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     */
    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // 设置自定义 userDetailsService，从数据库查询用户信息
//        daoAuthenticationProvider.setUserDetailsService(userService);
        // 设置密码加密器
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
         http.
            // 禁用 csrf防御，因为不使用session
                 csrf(AbstractHttpConfigurer::disable)
                 // 不通过Session获取SecurityContext
                 .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(authorize -> authorize
                  // 对于登录接口 允许匿名访问
                  .requestMatchers("/admin/webTitleSffix").permitAll()
                 .requestMatchers(HttpMethod.GET,"/admin/**").hasAnyRole("admin","visitor")
                 .requestMatchers("/admin/**").hasRole("admin")
                  // 除上面外的所有请求全部需要鉴权认证
                 .anyRequest().permitAll())
                 .addFilterBefore(new JwtLoginFilter("/admin/login",loginLogService), UsernamePasswordAuthenticationFilter.class)
                 .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
//                  配置异常处理器
                 .exceptionHandling(a-> a.authenticationEntryPoint(new MyAuthenticationEntryPoint()));
         return http.build();
    }
}
