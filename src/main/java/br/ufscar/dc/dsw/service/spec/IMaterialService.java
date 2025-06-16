package br.ufscar.dc.dsw.service.spec;

import br.ufscar.dc.dsw.domain.Material;
import java.util.List;

public interface IMaterialService {
    Material buscarPorId(Long id);
    List<Material> buscarTodos();
    void salvar(Material material);
    void excluir(Long id);
}