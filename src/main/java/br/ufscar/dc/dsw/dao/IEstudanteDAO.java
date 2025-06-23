package br.ufscar.dc.dsw.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.ufscar.dc.dsw.domain.Estudante;

@SuppressWarnings("unchecked")
public interface IEstudanteDAO extends JpaRepository<Estudante, Long> {
    Estudante findById(long id);
    Estudante findByCpf(String CPF);
    Estudante findByEmail(String email);
    boolean existsByCpf(String cpf);
    List<Estudante> findAll();
    Estudante save(Estudante estudante);
    void deleteById(Long id);
}