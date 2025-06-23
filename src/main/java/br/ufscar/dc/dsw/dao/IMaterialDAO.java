package br.ufscar.dc.dsw.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;

public interface IMaterialDAO extends JpaRepository<Material, Long> {
    List<Material> findByCategoria(Categoria categoria);
    List<Material> findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String titulo, String descricao);
    List<Material> findByEstudante(Estudante estudante);
}