package com.wallet.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.wallet.entity.User;
import com.wallet.enums.RoleEnum;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getEmail(), user.getPassword(), new ArrayList<GrantedAuthority>());
    }

    private static List<GrantedAuthority> createGrantedAuthorities(RoleEnum role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

}