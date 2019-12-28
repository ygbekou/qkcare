package com.qkcare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "userService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/", "/index.html", "/assets/*", "/static/index.html", "/home", "/about").permitAll()
                .antMatchers("/css/**", "/js/**","/images/**").permitAll()
                .antMatchers("/service/token/*").permitAll()
                .antMatchers("/service/user/forgot/*").permitAll() 
                .antMatchers("/service/user/forgot/changePassword").permitAll() 
                .antMatchers("/service/com.qkcare.model.website.SectionItem/allByCriteriaAndOrderBy").permitAll()
                .antMatchers("/service/com.qkcare.model.website.Section/allByCriteriaAndOrderBy").permitAll()
                .antMatchers("/service/com.qkcare.model.website.SliderText/allByCriteriaAndOrderBy").permitAll()
                .antMatchers("/service/Employee/allByCriteriaAndOrderBy").permitAll()
                .antMatchers("/service/Hospital/allByCriteriaAndOrderBy").permitAll()
                .antMatchers("/service/Department/all").permitAll()
                .antMatchers("/service/Company/*").permitAll()  
                .antMatchers("/service/user/User/*").permitAll()    
                .antMatchers("/service/UserGroup/all").permitAll()
                .antMatchers("/service/ContactUsMessage/save").permitAll()
                .antMatchers("/service/appointment/*").permitAll()
                .antMatchers("/service/radiology/investigation/get/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
