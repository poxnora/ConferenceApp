package com.example.conferenceapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig{


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("user")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$15$wxf4ygLmpRl0wAsxUoJETejEFCSDsoeCbdZYGcxG.XFCHgg6l5r1e")
                .roles("USER", "ADMIN")
                .build();
        UserDetails prof = User.builder()
                .username("professor")
                .password("{bcrypt}$2a$15$vreNMk4agKRcudcJ7o8q1OxQXhefteIW6WJq/9LP1UqzJRMV8jvCy")
                .roles("USER", "PROF")
                .build();
        return new InMemoryUserDetailsManager(user,prof,admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated()
                .antMatchers("/conference").hasAnyRole("USER","PROF","ADMIN")
                .antMatchers("/lectures").hasAnyRole("USER","PROF","ADMIN")
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .headers().frameOptions().disable()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .cors()
                .and().csrf().disable().formLogin();

        return httpSecurity.build();
    }
}
