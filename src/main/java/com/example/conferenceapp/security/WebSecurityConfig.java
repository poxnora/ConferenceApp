package com.example.conferenceapp.security;

import com.example.conferenceapp.model.User;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private SecurityUserService securityUserService;

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
                .antMatchers("/lectures").hasAnyRole("USER", "PROF", "ADMIN")
                .antMatchers("/users").hasAnyRole("PROF", "ADMIN")
                .antMatchers("lectures/show").hasRole("ADMIN")
                .antMatchers("lectures/show/*").hasRole("ADMIN")
                .antMatchers("lectures/popularity").hasAnyRole("PROF", "ADMIN")
                .antMatchers("lectures/theme_popularity").hasAnyRole("PROF", "ADMIN")
                .antMatchers("lectures/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("lectures/add").hasRole("ADMIN")
                .antMatchers("lectures/add/*").hasRole("ADMIN")
                .antMatchers("lectures/*/add_user").hasAnyRole("USER", "ADMIN")
                .antMatchers("lectures/*/cancel_user").hasAnyRole("USER", "PROF", "ADMIN")
                .antMatchers("lectures/delete/*").hasRole("ADMIN")
                .antMatchers("users/show").hasRole("ADMIN")
                .antMatchers("users/show/*").hasRole("ADMIN")
                .antMatchers("users/add").hasRole("ADMIN")
                .antMatchers("users/add/*").hasRole("ADMIN")
                .antMatchers("users/email").hasAnyRole("USER", "ADMIN")
                .antMatchers("users/delete/*").hasRole("ADMIN")
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated().and().csrf().disable().headers().frameOptions().disable().and()
                .httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;
    }
}
