package br.ufscar.dc.dsw.security;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.ufscar.dc.dsw.domain.Estudante;

public class MyUserDetails implements UserDetails {

    private Estudante estudante;

    public MyUserDetails(Estudante estudante) {
        this.estudante = estudante;
    }

    @Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority(estudante.getRole()));
}

    @Override
    public String getPassword() {
        return estudante.getSenha();
    }

    @Override
    public String getUsername() {
        return estudante.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}