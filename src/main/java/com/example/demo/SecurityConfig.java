package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**","/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) ->form
                                .loginPage("/login")
                                .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout((logout) ->logout.permitAll());
        return http.build();
    }


    @Autowired
    private UserDetailsService userDetailsService;

    /**
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    **/

    /**
    @Bean
    public UserDetailsService user() {
        UserDetails user = User.builder()
                .username("yama3")
                .password(passwordEncoder().encode("123456"))
                .roles("USER")
                .build();
        System.out.println(passwordEncoder().encode("123456"));
        return new InMemoryUserDetailsManager(user);
    }**/
}
