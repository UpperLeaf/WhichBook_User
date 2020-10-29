package com.econovation.whichbook_user.infra.config.security;

import com.econovation.whichbook_user.infra.config.security.filter.LoginAuthenticationFilter;
import com.econovation.whichbook_user.infra.config.security.handler.JwtLogoutHandler;
import com.econovation.whichbook_user.infra.config.security.handler.LoginSuccessHandler;
import com.econovation.whichbook_user.infra.config.security.provider.LoginAuthenticationProvider;
import com.econovation.whichbook_user.infra.utils.CookieUtils;
import com.econovation.whichbook_user.infra.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LogoutHandler jwtLogoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable().csrf().disable()
                .httpBasic().disable()
                .logout().logoutUrl("/user/logout").addLogoutHandler(jwtLogoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .and()
                    .authorizeRequests(authorize -> authorize
                        .mvcMatchers("/auth/**", "/user/signup", "/user/login").permitAll()
                        .anyRequest().authenticated());
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
