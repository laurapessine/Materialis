package br.ufscar.dc.dsw.service.spec;

import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;

import java.util.List;

public interface IMaterialService {
    Material buscarPorId(Long id);
    List<Material> buscarTodos();
    List<Material> buscarPorCategoria(Categoria categoria);
    List<Material> buscarPorPalavraChave(String palavra);
    void salvar(Material material);
    void excluir(Long id);
}