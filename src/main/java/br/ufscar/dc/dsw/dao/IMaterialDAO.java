package br.ufscar.dc.dsw.dao;

import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Estudante;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface IMaterialDAO extends CrudRepository<Material, Long> {
    List<Material> findByCategoria(Material.Categoria categoria);

    List<Material> findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String titulo, String descricao);

    List<Material> findByEstudante(Estudante estudante);
}