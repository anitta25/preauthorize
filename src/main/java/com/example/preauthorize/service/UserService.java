package com.example.preauthorize.service;

import com.example.preauthorize.DTO.LoginRequestDTO;
import com.example.preauthorize.DTO.SignupRequestDTO;
import com.example.preauthorize.configuration.AuthorityCollection;
import com.example.preauthorize.entity.User;
import com.example.preauthorize.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo repo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepo;
    public User signup(SignupRequestDTO signupRequestDTO) {
        if (Optional.ofNullable(repo.findByUsername(signupRequestDTO.getUsername())).isPresent())
        {
            return  null;
        }
        else
        { User user=new User();
           user.setUsername(signupRequestDTO.getUsername());
           user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
           user.setAuthority(signupRequestDTO.getAuthority());

           repo.save(user);
           return  user;
        }
    }

    public User login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),loginRequestDTO.getPassword(),null);
       try {
           Authentication authentication = authenticationManager.authenticate(token);
           SecurityContextHolder.getContext().setAuthentication(authentication);
           for (GrantedAuthority authority : authentication.getAuthorities()) {
               System.out.println("authority"+ authority.getAuthority());}
          return userRepo.findByUsername(loginRequestDTO.getUsername());

       }catch (AuthenticationException exception)
       {
           return null;
       }
    }

}



