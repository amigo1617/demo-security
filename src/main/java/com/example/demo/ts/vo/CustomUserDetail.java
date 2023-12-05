package com.example.demo.ts.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomUserDetail implements UserDetails {

    private String username;

    private String password;

    private String address;

    private String auth;

    public CustomUserDetail(Map<String,String> userMap) {
        this.username = userMap.get("USERNAME");
        this.password = userMap.get("PASSWORD");
        this.address = userMap.get("ADDRESS");
        this.auth = userMap.get("AUTH");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.auth));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
