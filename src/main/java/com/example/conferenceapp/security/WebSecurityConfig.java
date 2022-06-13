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
                .antMatchers("/").permitAll()
                .antMatchers("/lectures").hasAnyAuthority("USER", "PROF", "ADMIN")
                .antMatchers("/users").hasAnyAuthority("PROF", "ADMIN")
                .antMatchers("/lectures/show").hasAnyAuthority("ADMIN")
                .antMatchers("/lectures/show/*").hasAnyAuthority("ADMIN")
                .antMatchers("/lectures/popularity").hasAnyAuthority("PROF", "ADMIN")
                .antMatchers("/lectures/theme_popularity").hasAnyAuthority("PROF", "ADMIN")
                .antMatchers("/lectures/user").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/lectures/add").hasAnyAuthority("ADMIN")
                .antMatchers("/lectures/add/*").hasAnyAuthority("ADMIN")
                .antMatchers("/lectures/*/add_user").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/lectures/*/cancel_user").hasAnyAuthority("USER", "PROF", "ADMIN")
                .antMatchers("/lectures/delete/*").hasAnyAuthority("ADMIN")
                .antMatchers("/users/show").hasAnyAuthority("ADMIN")
                .antMatchers("/users/show/*").hasAnyAuthority("ADMIN")
                .antMatchers("/users/add").hasAnyAuthority("ADMIN")
                .antMatchers("/users/add/*").hasAnyAuthority("ADMIN")
                .antMatchers("/users/email").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/users/delete/*").hasAnyAuthority("ADMIN")
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated().and().csrf().disable().headers().frameOptions().disable().and()
                .httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;
    }
}
