package com.example.preauthorize.controller;

import com.example.preauthorize.DTO.LoginRequestDTO;
import com.example.preauthorize.DTO.SignupRequestDTO;
import com.example.preauthorize.entity.User;

import com.example.preauthorize.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j

public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO)
    {
        User user=userService.signup(signupRequestDTO);
        if (user==null)
        return ResponseEntity.status(HttpStatus.CONFLICT).body("user already exists");
        else {
            Map<String, Object> response = new HashMap<>();
            response.put("message","successfully created");
            response.put("body",user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpSession session)
    {  log.info("inside login");
        User admin= userService.login(loginRequestDTO);
        if (admin==null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("fail");
        else {session.setAttribute("securitycontext", SecurityContextHolder.getContext());
            return ResponseEntity.status(HttpStatus.OK).body("login success");
        }



    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/adminpage")
    public String adminpage() {
        return "adminpage";
    }
}
