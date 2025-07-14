package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Emprestimo.Status;
import br.ufscar.dc.dsw.service.spec.IEmprestimoService;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
//import jakarta.validation.Valid;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {
    @Autowired
    private IEmprestimoService emprestimoService;

    @Autowired
    private IEstudanteService estudanteService;

    @Autowired
    private IMaterialService materialService;

    private Estudante getLoggedInEstudante() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String userEmail = authentication.getName();
        return estudanteService.buscarPorEmail(userEmail);
    }

    @GetMapping("/solicitar/{materialId}")
    public String solicitarEmprestimo(@PathVariable("materialId") Long materialId, ModelMap model, RedirectAttributes attr) {
        Material material = materialService.buscarPorId(materialId);
        Estudante estudante = getLoggedInEstudante();
        if (material == null) {
            attr.addFlashAttribute("fail", "Material não encontrado.");
            return "redirect:/materiais/listar";
        }
        if (estudante == null) {
            attr.addFlashAttribute("fail", "Você precisa estar logado como estudante para solicitar um empréstimo.");
            return "redirect:/login";
        }
        if (emprestimoService.hasOpenProposal(estudante, material)) {
            attr.addFlashAttribute("fail", "Você já tem uma proposta em aberto para este material.");
            return "redirect:/materiais/listar";
        }

        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("material", material);

        model.addAttribute("material", material);
        return "emprestimo/solicitar";
    }

    @PostMapping("/salvarSolicitacao")
    public String salvarSolicitacao(Emprestimo emprestimo, @RequestParam("materialId") Long materialId, RedirectAttributes attr) {
        Estudante estudante = getLoggedInEstudante();
        Material material = materialService.buscarPorId(materialId);
        if (estudante == null || material == null) {
            attr.addFlashAttribute("fail", "Erro: Usuário ou material inválido.");
            return "redirect:/materiais/listar";
        }
        if (emprestimoService.hasOpenProposal(estudante, material)) {
            attr.addFlashAttribute("fail", "Você já tem uma proposta em aberto para este material.");
            return "redirect:/materiais/listar";
        }
        emprestimo.setEstudante(estudante);
        emprestimo.setMaterial(material);
    
        emprestimoService.salvar(emprestimo);
        
        attr.addFlashAttribute("sucesso", "Solicitação de empréstimo enviada com sucesso!");
        return "redirect:/emprestimos/meus-emprestimos";
    }


    @GetMapping("/listar")
    public String listarTodosEmprestimos(ModelMap model) {
        model.addAttribute("emprestimos", emprestimoService.buscarTodos());
        return "emprestimo/lista";
    }

    @GetMapping("/meus-emprestimos")
    public String listarMeusEmprestimos(ModelMap model) {
        Estudante estudante = getLoggedInEstudante();
        if (estudante == null) {
            model.addAttribute("fail", "Você precisa estar logado para ver seus empréstimos.");
            return "redirect:/login";
        }
        model.addAttribute("emprestimos", emprestimoService.buscarPorEstudante(estudante));
        return "emprestimo/meus-emprestimos";
    }

    @GetMapping("/solicitacoes-para-meus-materiais")
    public String listarSolicitacoesParaMeusMateriais(ModelMap model) {
        Estudante estudanteDoador = getLoggedInEstudante();
        if (estudanteDoador == null) {
            model.addAttribute("fail", "Você precisa estar logado para gerenciar solicitações.");
            return "redirect:/login";
        }
        model.addAttribute("solicitacoes", emprestimoService.buscarSolicitacoesParaEstudanteDoador(estudanteDoador));
        return "emprestimo/gerenciar-solicitacoes";
    }

    @PostMapping("/aprovar/{id}")
    public String aprovarSolicitacao(@PathVariable("id") Long id, RedirectAttributes attr) {
        System.out.println("\n--- [DEBUG] AÇÃO DE APROVAR INICIADA ---");
        System.out.println("   - ID do Empréstimo recebido: " + id);

        Emprestimo emprestimo = emprestimoService.buscarPorId(id);

        if (emprestimo != null) {
            System.out.println("   - Empréstimo encontrado no banco.");
            emprestimo.setStatus(Status.APROVADO);
            emprestimoService.salvar(emprestimo);
            System.out.println("   - Status alterado para APROVADO e salvo.");
            attr.addFlashAttribute("sucesso", "Solicitação aprovada com sucesso.");
        } else {
            System.out.println("   - FALHA: Empréstimo com ID " + id + " não encontrado.");
            attr.addFlashAttribute("fail", "Não foi possível encontrar a solicitação para aprovar.");
        }

        return "redirect:/emprestimos/solicitacoes-para-meus-materiais";
    }

    @PostMapping("/recusar/{id}")
    public String recusarSolicitacao(@PathVariable("id") Long id, RedirectAttributes attr) {
        System.out.println("\n--- [DEBUG] AÇÃO DE RECUSAR INICIADA ---");
        System.out.println("   - ID do Empréstimo recebido: " + id);
        
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);

        if (emprestimo != null) {
            System.out.println("   - Empréstimo encontrado no banco.");
            emprestimo.setStatus(Status.RECUSADO);
            emprestimoService.salvar(emprestimo);
            System.out.println("   - Status alterado para RECUSADO e salvo.");
            attr.addFlashAttribute("sucesso", "Solicitação recusada com sucesso.");
        } else {
            System.out.println("   - FALHA: Empréstimo com ID " + id + " não encontrado.");
            attr.addFlashAttribute("fail", "Não foi possível encontrar a solicitação para recusar.");
        }

        return "redirect:/emprestimos/solicitacoes-para-meus-materiais";
    }
    @PostMapping("/iniciar/{id}")
    public String iniciarEmprestimo(@PathVariable("id") Long id, RedirectAttributes attr) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);

        if (emprestimo != null && emprestimo.getStatus() == Emprestimo.Status.APROVADO) {
            emprestimo.setStatus(Emprestimo.Status.EM_ANDAMENTO);
            emprestimoService.salvar(emprestimo);
            attr.addFlashAttribute("sucesso", "Empréstimo iniciado com sucesso!");
        } else {
            attr.addFlashAttribute("fail", "Não foi possível iniciar o empréstimo.");
        }

        return "redirect:/emprestimos/meus-emprestimos";
    }

    @PostMapping("/concluir/{id}")
    public String concluirEmprestimo(@PathVariable("id") Long id, RedirectAttributes attr) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);

        if (emprestimo != null && emprestimo.getStatus() == Emprestimo.Status.EM_ANDAMENTO) {
            emprestimo.setStatus(Emprestimo.Status.CONCLUIDO);
            emprestimo.setDataDevolucaoReal(java.time.LocalDate.now());
            emprestimoService.salvar(emprestimo);
            attr.addFlashAttribute("sucesso", "Empréstimo concluído com sucesso!");
        } else {
            attr.addFlashAttribute("fail", "Não foi possível concluir o empréstimo.");
        }

        return "redirect:/emprestimos/meus-emprestimos";
    }
}