package com.hepsiemlak.todo.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hepsiemlak.todo.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    public String id;
    private String username;
    @JsonIgnore
    private String password;

    public Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl create(User user){
        List<GrantedAuthority> authorityList = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), authorityList);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
