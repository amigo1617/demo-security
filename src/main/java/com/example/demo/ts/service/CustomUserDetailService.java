package com.example.demo.ts.service;

import com.example.demo.ts.vo.CustomUserDetail;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private TsService tsService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String,String> userMap = tsService.selectUser(username);
        if (userMap == null) throw new UsernameNotFoundException(String.format("user '%s' is not register of wrong password ", username));
        return new CustomUserDetail(userMap);
    }
}
