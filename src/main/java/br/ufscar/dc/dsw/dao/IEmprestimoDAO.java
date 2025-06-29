package br.ufscar.dc.dsw.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;

public interface IEmprestimoDAO extends JpaRepository<Emprestimo, Long> {
    Emprestimo findById(long id);
    List<Emprestimo> findByEstudante(Estudante estudante);
    List<Emprestimo> findByMaterial_Estudante(Estudante estudanteDoador);
    boolean existsByEstudanteAndMaterialAndStatus(Estudante estudante, Material material, Emprestimo.Status status);
}