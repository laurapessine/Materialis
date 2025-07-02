package br.ufscar.dc.dsw.service.spec;

import java.util.List;
import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;

public interface IEmprestimoService {
    Emprestimo buscarPorId(Long id);

    List<Emprestimo> buscarTodos();

    void salvar(Emprestimo emprestimo);

    void excluir(Long id);

    List<Emprestimo> buscarPorEstudante(Estudante estudante);

    List<Emprestimo> buscarSolicitacoesParaEstudanteDoador(Estudante estudanteDoador);

    boolean hasOpenProposal(Estudante estudante, Material material);

    void aprovarEmprestimo(Long id);

    void recusarEmprestimo(Long id, String justificativa);

    void iniciarEmprestimo(Long id);

    void concluirEmprestimo(Long id);
}