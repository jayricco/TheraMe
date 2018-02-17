package com.therame.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class DetailedUserDetails extends org.springframework.security.core.userdetails.User {

    @Getter
    private User user;

    public DetailedUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }

    public DetailedUserDetails(User user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
    }
}
