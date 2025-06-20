package br.ufscar.dc.dsw.service.spec;

import br.ufscar.dc.dsw.domain.Estudante;
import java.util.List;

public interface IEstudanteService {
    Estudante buscarPorId(Long id);
	List<Estudante> buscarTodos();
    Estudante buscarPorCPF(String cpf);
	void salvar(Estudante estudante);
	void excluir(Long id);
    boolean estudanteTemMaterial(Long id);
}
