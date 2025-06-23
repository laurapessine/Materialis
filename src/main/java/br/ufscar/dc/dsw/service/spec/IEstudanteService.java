package br.ufscar.dc.dsw.service.spec;

import br.ufscar.dc.dsw.domain.Estudante;
import java.util.List;

public interface IEstudanteService {
    Estudante buscarPorId(Long id);
    Estudante buscarPorCPF(String cpf);
    Estudante buscarPorEmail(String email);
    List<Estudante> buscarTodos();
    void salvar(Estudante estudante);
    void excluir(Long id);
    boolean estudanteTemMaterial(Long id);
}