package br.ufscar.dc.dsw.dao;

import br.ufscar.dc.dsw.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IMaterialDAO extends JpaRepository<Material, Long> {
    Material findById(long id);
    List<Material> findByCategoria(Material.Categoria categoria);
    List<Material> findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String titulo, String descricao);
}