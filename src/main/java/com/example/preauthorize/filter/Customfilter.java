package com.example.preauthorize.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class Customfilter  extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      HttpSession session=request.getSession();
      log.info("inside filter");
        SecurityContext context= (SecurityContext) session.getAttribute("securitycontext");
        if(context==null)
            log.info("context null");
        if (context!=null)
        { log.info("context not null");
            Authentication authentication=context.getAuthentication();
            if (authentication.isAuthenticated())
                log.info("success");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
                System.out.println("authority1"+ authority.getAuthority());}
        }
      filterChain.doFilter(request,response);
    }
}
