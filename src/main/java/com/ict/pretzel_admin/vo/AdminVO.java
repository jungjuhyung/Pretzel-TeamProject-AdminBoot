package com.ict.pretzel_admin.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class AdminVO implements UserDetails{
    private String admin_idx, admin_id, pwd, name, role, note, regdate, status, user_id;

    private List<GrantedAuthority> authorities = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {       return authorities;    }
    @Override
    public String getPassword() {          return pwd;    }
    @Override
    public String getUsername() {        return admin_id;    }
    @Override
    public boolean isAccountNonExpired() {        return true;    }
    @Override
    public boolean isAccountNonLocked() {        return true;    }
    @Override
    public boolean isCredentialsNonExpired() {        return true;    }
    @Override
    public boolean isEnabled() {        return true;    }
}
