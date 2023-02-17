package com.gusta.auth.service;

import com.gusta.auth.model.entity.*;
import com.gusta.auth.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

        if (user == null) throw new UsernameNotFoundException("User not found!");
        return new CustomUserDetails(user);
    }
}
