package com.example.preauthorize.configuration;

import com.example.preauthorize.filter.Customfilter;
import com.example.preauthorize.service.UserdetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    UserdetService userdetService;
    @Autowired
    Customfilter customfilter;
    @Bean
    public PasswordEncoder passwordEncoder() throws  Exception
    {
        return  new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception
    {
        httpSecurity                                    .csrf(AbstractHttpConfigurer::disable);
        httpSecurity                                   .authorizeHttpRequests((req)->req
                .requestMatchers("/adminpage").authenticated()

                .anyRequest().permitAll());
        httpSecurity  .addFilterBefore(customfilter, BasicAuthenticationFilter.class);

        httpSecurity .httpBasic(Customizer.withDefaults());
        return  httpSecurity.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws  Exception
    {
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userdetService);
        return  provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws  Exception
    {
        AuthenticationManagerBuilder authenticationManagerBuilder=httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }
}
