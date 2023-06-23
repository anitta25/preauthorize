package com.example.preauthorize.service;

import com.example.preauthorize.configuration.AuthorityCollection;
import com.example.preauthorize.entity.User;
import com.example.preauthorize.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserdetService implements UserDetailsService {
    @Autowired
    AuthorityCollection authorityCollection;
    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= Optional.ofNullable(userRepo.findByUsername(username))
                .orElseThrow(()->new UsernameNotFoundException("username not found"));
        return  new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorityCollection.conversion(user.getAuthority()));
    }
}
