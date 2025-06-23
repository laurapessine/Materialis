package br.ufscar.dc.dsw.service.spec;

import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.domain.Estudante;
import java.util.List;

public interface IMaterialService {
    Material buscarPorId(Long id);
    List<Material> buscarTodos();
    List<Material> buscarPorCategoria(Categoria categoria);
    List<Material> buscarPorPalavraChave(String palavra);
    List<Material> buscarDisponiveis(Categoria categoria, String palavra);
    List<Material> buscarPorDono(Estudante estudante);
    void salvar(Material material);
    void excluir(Long id);
}