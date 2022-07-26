package com.example.conferenceapp.security;

import com.example.conferenceapp.service.user.implementation.SecurityUserServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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


    private final SecurityUserServiceImp securityUserServiceImp;


    public WebSecurityConfig(SecurityUserServiceImp securityUserServiceImp) {
        this.securityUserServiceImp = securityUserServiceImp;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(securityUserServiceImp);
        auth.inMemoryAuthentication().withUser("test").password("test").roles("ADMIN");


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/conference").permitAll()
                .mvcMatchers(HttpMethod.POST, "/conference").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/conference").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.DELETE, "/conference/**").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/lectures").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/lectures/**").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/lectures/titles").hasAnyAuthority("USER", "PROF", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/users").hasAnyAuthority("PROF", "ADMIN")
                .mvcMatchers(HttpMethod.POST, "/lectures").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/lectures/**").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/lectures/*/users").hasAnyAuthority("USER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/lectures/*/users/cancellation").hasAnyAuthority("USER", "PROF", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/lectures/popularity").hasAnyAuthority("PROF", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/lectures/theme_popularity").hasAnyAuthority("PROF", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/users").hasAnyAuthority("PROF", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/users/details").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/users/lectures").hasAnyAuthority("USER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/users/details/**").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/users").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/users/change_email").hasAnyAuthority("USER", "ADMIN")
                .mvcMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated().and().csrf().disable().headers().frameOptions().disable().and()
                .httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;
    }
}
