package com.example.conferenceapp.security;

import com.example.conferenceapp.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    public WebSecurityConfig(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }


    private final SecurityUserService securityUserService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(securityUserService);


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .authorizeRequests()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/lectures").hasAnyAuthority("USER", "PROF", "ADMIN")
                .mvcMatchers("/users").hasAnyAuthority("PROF", "ADMIN")
                .mvcMatchers("/lectures/show").hasAnyAuthority("ADMIN")
                .mvcMatchers("/lectures/show/**").hasAnyAuthority("ADMIN")
                .mvcMatchers("/lectures/popularity").hasAnyAuthority("PROF", "ADMIN")
                .mvcMatchers("/lectures/theme_popularity").hasAnyAuthority("PROF", "ADMIN")
                .mvcMatchers("/lectures/user").hasAnyAuthority("USER", "ADMIN")
                .mvcMatchers("/lectures/add").hasAnyAuthority("ADMIN")
                .mvcMatchers("/lectures/add/**").hasAnyAuthority("ADMIN")
                .mvcMatchers("/lectures/*/add_user").hasAnyAuthority("USER", "ADMIN")
                .mvcMatchers("/lectures/*/cancel_user").hasAnyAuthority("USER", "PROF", "ADMIN")
                .mvcMatchers("/lectures/delete/**").hasAnyAuthority("ADMIN")
                .mvcMatchers("/users/show").hasAnyAuthority("ADMIN")
                .mvcMatchers("/users/show/**").hasAnyAuthority("ADMIN")
                .mvcMatchers("/users/add").hasAnyAuthority("ADMIN")
                .mvcMatchers("/users/add/**").hasAnyAuthority("ADMIN")
                .mvcMatchers("/users/email").hasAnyAuthority("USER", "ADMIN")
                .mvcMatchers("/users/delete/**").hasAnyAuthority("ADMIN")
                .mvcMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated().and().csrf().disable().headers().frameOptions().disable().and()
                .httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;
    }
}
