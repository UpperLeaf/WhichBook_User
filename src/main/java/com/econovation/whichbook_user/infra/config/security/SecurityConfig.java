package com.econovation.whichbook_user.infra.config.security;

import com.econovation.whichbook_user.infra.config.security.filter.CustomAuthenticationFilter;
import com.econovation.whichbook_user.infra.config.security.handler.CustomAuthenticationSuccessHandler;
import com.econovation.whichbook_user.infra.config.security.provider.CustomAuthenticationProvider;
import com.econovation.whichbook_user.infra.utils.CookieUtils;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable().csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .mvcMatchers("/auth/**", "/user/signup", "/user/login").permitAll()
                        .anyRequest().authenticated());
    }


    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(ObjectMapper objectMapper, JwtTokenUtils jwtTokenUtils, CookieUtils cookieUtils) throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManager(), objectMapper);
        filter.setFilterProcessesUrl("/user/login");
        filter.setAuthenticationSuccessHandler(successHandler(jwtTokenUtils, cookieUtils));
        return filter;
    }

    @Bean
    public CustomAuthenticationSuccessHandler successHandler(JwtTokenUtils jwtTokenUtils, CookieUtils cookieUtils) {
        return new CustomAuthenticationSuccessHandler(jwtTokenUtils, cookieUtils);
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(UserDetailsService userServiceImpl) {
        return new CustomAuthenticationProvider(userServiceImpl, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
