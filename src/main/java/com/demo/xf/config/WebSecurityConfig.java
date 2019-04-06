package com.demo.xf.config;

import com.demo.xf.filters.JWTAuthenticationFilter;
import com.demo.xf.filters.JWTLoginFilter;
import com.demo.xf.provider.CustomProvider;
import com.demo.xf.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Author: xiongfeng
 * Date: 2019/4/4 10:21
 * Description:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CustomProvider customProvider;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                .withUser("admin")
                .password("$2a$10$BZVDjzE3BBdEI84sXT0HPuSnnICzT6pPeAHCNAfL7P4FitQ6XwoAS")
                .roles("ADMIN");*/
        auth.authenticationProvider(customProvider);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAt(new JWTLoginFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .exceptionHandling().
                accessDeniedHandler(new CustomAccessDeniedHandler())
               .authenticationEntryPoint(new CustomAuthenticationHandler());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public static void output(Result result, HttpServletResponse response) throws IOException{
        String jsonStr = objectMapper.writeValueAsString(result);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().append(jsonStr);
    }

    static class CustomAccessDeniedHandler implements AccessDeniedHandler{
        @Override
        public void handle(HttpServletRequest request,
                           HttpServletResponse response,
                           AccessDeniedException e) throws IOException, ServletException {
            if (!response.isCommitted()){
                response.setStatus(401);
                Result result = new Result(HttpStatus.FORBIDDEN.value(), e.getMessage());
                WebSecurityConfig.output(result, response);
            }

        }
    }

    static class CustomAuthenticationHandler implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException e) throws IOException, ServletException {
            response.setStatus(403);
            Result result = new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户未登录");
            WebSecurityConfig.output(result, response);
        }

        private static boolean isAjaxRequest(HttpServletRequest request) {
            String ajaxFlag = request.getHeader("X-Requested-With");
            return ajaxFlag != null && "XMLHttpRequest".equals(ajaxFlag);
        }
    }

}
