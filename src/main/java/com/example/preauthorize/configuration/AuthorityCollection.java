package com.example.preauthorize.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class AuthorityCollection {
    public Collection<? extends GrantedAuthority> conversion (String authority)
    {   Collection <GrantedAuthority> collection=new ArrayList<>();
        collection.add(new SimpleGrantedAuthority(authority));
        return  collection;
    }
}
