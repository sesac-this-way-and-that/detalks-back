package com.twat.detalks.oauth2.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class CustomUserDetail implements OAuth2User, UserDetails {

    private final UserDTO userDTO;

    public CustomUserDetail(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getUserName() {
        return userDTO.getUsername();
    }

    public String getUserIdx() {
        return userDTO.getIdx();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(getUserIdx());
    }
}
