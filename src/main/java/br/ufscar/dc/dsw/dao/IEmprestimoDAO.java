package br.ufscar.dc.dsw.dao;

import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IEmprestimoDAO extends JpaRepository<Emprestimo, Long> {
    Emprestimo findById(long id);
    List<Emprestimo> findByEstudante(Estudante estudante);
    List<Emprestimo> findByMaterial_Estudante(Estudante estudanteDoador);
    boolean existsByEstudanteAndMaterialAndStatus(Estudante estudante, Material material, Emprestimo.Status status);
}