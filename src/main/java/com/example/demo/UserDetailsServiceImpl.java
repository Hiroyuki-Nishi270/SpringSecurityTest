package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            String sql = "SELECT * FROM user WHERE name = ?";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, username);
            String password = (String)map.get("password");
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
            return new UserDetailsImpl(username, password, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found.", e);
        }
    }

    @Transactional
    public void register(String username, String password, String authority) {
        String sql = "INSERT INTO user(name, password, authority) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, username, passwordEncoder().encode(password), authority);
    }

    public boolean isExistUser(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE name = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { username });
        if (count == 0) {
            return false;
        }
        return true;
    }
}
