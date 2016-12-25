package com.fxp.ourpiece.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable()
        
            .authorizeRequests()
                .anyRequest().authenticated()//every request requires the user to be authenticated
                .and()
            .formLogin()//form based authentication is supported
                .loginPage("/login").defaultSuccessUrl("/chat", true)
                .permitAll();
    }
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//基于jdbc认证http://stackoverflow.com/questions/22749767/using-jdbcauthentication-in-spring-security-with-hibernate
		auth
			.inMemoryAuthentication()
				.withUser("fxp").password("fxp").roles("USER").and()
				.withUser("whc").password("whc").roles("USER").and()
				.withUser("fuy").password("fuy").roles("USER").and()
				.withUser("admin").password("admin").roles("ADMIN","USER");
	}
}
