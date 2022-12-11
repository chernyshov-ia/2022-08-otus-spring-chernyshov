package ru.otus.books.services;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .credentialsExpired(false)
                .accountLocked(false)
                .roles()
                .authorities(List.of())
                .build();

        return userDetails;
    }
}
