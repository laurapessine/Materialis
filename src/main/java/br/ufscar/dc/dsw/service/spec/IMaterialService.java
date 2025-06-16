package br.ufscar.dc.dsw.service.spec;

import br.ufscar.dc.dsw.domain.Material;
import java.util.List;

public interface IMaterialService {
    Material buscarPorId(Long id);
    List<Material> buscarTodos();
    List<Material> buscarPorCategoria(String categoria);
    List<Material> buscarPorPalavraChave(String palavra);
    void salvar(Material material);
    void excluir(Long id);
}