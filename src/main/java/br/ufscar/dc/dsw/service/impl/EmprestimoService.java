package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IEmprestimoDAO;
import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.service.spec.IEmprestimoService;
import br.ufscar.dc.dsw.service.spec.IEmailService; // Import EmailService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmprestimoService implements IEmprestimoService {

    @Autowired
    private IEmprestimoDAO dao;

    @Autowired
    private IEmailService emailService;

    @Override
    public Emprestimo buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public List<Emprestimo> buscarTodos() {
        return dao.findAll();
    }

    @Override
    public void salvar(Emprestimo emprestimo) {
        dao.save(emprestimo);
    }

    @Override
    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Override
    public List<Emprestimo> buscarPorEstudante(Estudante estudante) {
        return dao.findByEstudante(estudante);
    }

    @Override
    public List<Emprestimo> buscarSolicitacoesParaEstudanteDoador(Estudante estudanteDoador) {
        return dao.findByMaterial_Estudante(estudanteDoador);
    }

    @Override
    public boolean hasOpenProposal(Estudante estudante, Material material) {
        return dao.existsByEstudanteAndMaterialAndStatus(estudante, material, Emprestimo.Status.ABERTO);
    }

    @Override
    public void aprovarEmprestimo(Long id) {
        Optional<Emprestimo> optionalEmprestimo = dao.findById(id);
        if (optionalEmprestimo.isPresent()) {
            Emprestimo emprestimo = optionalEmprestimo.get();
            emprestimo.setStatus(Emprestimo.Status.APROVADO);
            dao.save(emprestimo);
            String subject = "Sua solicitação de empréstimo foi APROVADA!";
            String text = String.format("Olá %s,\n\nSua solicitação de empréstimo para o material '%s' foi aprovada. " +
                                        "Entre em contato com o doador para combinar a retirada.\n\nAtenciosamente,\nEquipe Materialis",
                                        emprestimo.getEstudante().getNome(), emprestimo.getMaterial().getTitulo());
            emailService.sendEmail(emprestimo.getEstudante().getEmail(), subject, text);
        }
    }

    @Override
    public void recusarEmprestimo(Long id, String justificativa) {
        Optional<Emprestimo> optionalEmprestimo = dao.findById(id);
        if (optionalEmprestimo.isPresent()) {
            Emprestimo emprestimo = optionalEmprestimo.get();
            emprestimo.setStatus(Emprestimo.Status.RECUSADO);
            dao.save(emprestimo);
            String subject = "Sua solicitação de empréstimo foi RECUSADA.";
            String text = String.format("Olá %s,\n\nSua solicitação de empréstimo para o material '%s' foi recusada.",
                                        emprestimo.getEstudante().getNome(), emprestimo.getMaterial().getTitulo());
            if (justificativa != null && !justificativa.isBlank()) {
                text += "\nJustificativa do doador: " + justificativa;
            }
            text += "\n\nAtenciosamente,\nEquipe Materialis";
            emailService.sendEmail(emprestimo.getEstudante().getEmail(), subject, text);
        }
    }

    @Override
    public void iniciarEmprestimo(Long id) {
        Optional<Emprestimo> optionalEmprestimo = dao.findById(id);
        if (optionalEmprestimo.isPresent()) {
            Emprestimo emprestimo = optionalEmprestimo.get();
            if (emprestimo.getStatus() == Emprestimo.Status.APROVADO) {
                emprestimo.setStatus(Emprestimo.Status.EM_ANDAMENTO);
                dao.save(emprestimo);
                String subject = "Seu empréstimo para o material '" + emprestimo.getMaterial().getTitulo() + "' está EM ANDAMENTO!";
                String text = String.format("Olá %s,\n\nSeu empréstimo para o material '%s' agora está em andamento. Lembre-se da data de devolução prevista: %s.\n\nAtenciosamente,\nEquipe Materialis",
                                            emprestimo.getEstudante().getNome(), emprestimo.getMaterial().getTitulo(), emprestimo.getDataDevolucaoPrevista().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                emailService.sendEmail(emprestimo.getEstudante().getEmail(), subject, text);
            }
        }
    }

    @Override
    public void concluirEmprestimo(Long id) {
        Optional<Emprestimo> optionalEmprestimo = dao.findById(id);
        if (optionalEmprestimo.isPresent()) {
            Emprestimo emprestimo = optionalEmprestimo.get();
            if (emprestimo.getStatus() == Emprestimo.Status.EM_ANDAMENTO) {
                emprestimo.setStatus(Emprestimo.Status.CONCLUIDO);
                emprestimo.setDataDevolucaoReal(LocalDate.now());
                dao.save(emprestimo);
                String subject = "Seu empréstimo para o material '" + emprestimo.getMaterial().getTitulo() + "' foi CONCLUÍDO!";
                String text = String.format("Olá %s,\n\nSeu empréstimo para o material '%s' foi marcado como concluído. Agradecemos por usar o Materialis!\n\nAtenciosamente,\nEquipe Materialis",
                                            emprestimo.getEstudante().getNome(), emprestimo.getMaterial().getTitulo());
                emailService.sendEmail(emprestimo.getEstudante().getEmail(), subject, text);
            }
        }
    }
}