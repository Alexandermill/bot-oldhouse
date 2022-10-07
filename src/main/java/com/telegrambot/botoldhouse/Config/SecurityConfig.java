package com.telegrambot.botoldhouse.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("flash4")
                .authorities("ROLE_ADMIN")
                .and()
                .withUser("user")
                .password("user44")
                .authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/db/**").hasAnyRole("ADMIN")
                .antMatchers("/h2-console/").hasAnyRole("ADMIN")
                .antMatchers("/web/**").hasAnyRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and().csrf().ignoringAntMatchers("/h2-console/**")
                .and().headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .formLogin();

    }
}
