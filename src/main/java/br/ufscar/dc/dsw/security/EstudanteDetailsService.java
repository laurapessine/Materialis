package br.ufscar.dc.dsw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ufscar.dc.dsw.dao.IEstudanteDAO;
import br.ufscar.dc.dsw.domain.Estudante;

@Service
public class EstudanteDetailsService implements UserDetailsService {

    @Autowired
    private IEstudanteDAO estudanteDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Estudante estudante = estudanteDAO.findByEmail(email);
        if (estudante == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
        }
        return new MyUserDetails(estudante);
    }
}