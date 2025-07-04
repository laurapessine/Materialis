package br.ufscar.dc.dsw.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;

public interface IMaterialDAO extends CrudRepository<Material, Long> {
    List<Material> findAll();

    List<Material> findByCategoria(Material.Categoria categoria);

    List<Material> findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String titulo, String descricao);

    List<Material> findByEstudante(Estudante estudante);

    List<Material> findByCategoriaAndTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(Categoria categoria, String titulo, String descricao);
}