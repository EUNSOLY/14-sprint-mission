package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.user.data.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DiscodietUserDetails implements UserDetails {
    private final UserDto userDto;
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userDto.role()));

    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return userDto.username();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiscodietUserDetails user) {
            return this.userDto.username().equals(user.getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.userDto.username().hashCode();
    }
}
