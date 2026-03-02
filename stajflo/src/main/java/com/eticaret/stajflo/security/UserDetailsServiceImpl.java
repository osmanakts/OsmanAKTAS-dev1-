package com.eticaret.stajflo.security;

import com.eticaret.stajflo.model.User;
import com.eticaret.stajflo.repository.UserRepository;


import com.eticaret.stajflo.model.User;
import com.eticaret.stajflo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. userRepository'den gelen Optional<User> nesnesini alıyoruz.
        // 2. orElseThrow() metodu ile:
        //    - Eğer kullanıcı varsa, User objesi elde edilir.
        //    - Eğer kullanıcı yoksa, belirtilen UsernameNotFoundException fırlatılır.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Bu noktada 'user' artık kesinlikle bir User objesidir.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                // Not: Rolleri bu şekilde tek bir Collection içinde tanımlamak en temiz yoldur.
                Collections.singletonList(() -> "ROLE_" + user.getRole())
        );
    }
}