package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.service.spec.IEmprestimoService;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private IEmprestimoService emprestimoService;

    @Autowired
    private IEstudanteService estudanteService;

    @Autowired
    private IMaterialService materialService;

    // private Estudante getLoggedInEstudante() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     String userEmail = authentication.getName();
    //     Estudante estudante = estudanteService.buscarPorCPF("123.141.928-23");
    //     if (estudante == null) {
    //         if (userEmail.equals("lorena@gmail.com")) {
    //             return estudanteService.buscarPorCPF("123.141.928-23");
    //         } else if (userEmail.equals("luis@gmail.com")) {
    //             return estudanteService.buscarPorCPF("182.283.192-01");
    //         }
    //     }
    //     return estudante;
    // }
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

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setMaterial(material);
        emprestimo.setEstudante(estudante);
        model.addAttribute("emprestimo", emprestimo);
        return "emprestimo/solicitar";
    }

    @PostMapping("/salvarSolicitacao")
    public String salvarSolicitacao(@Valid Emprestimo emprestimo, BindingResult result, RedirectAttributes attr, ModelMap model) {
        Estudante estudante = getLoggedInEstudante();
        if (estudante == null) {
             attr.addFlashAttribute("fail", "Sessão de estudante inválida.");
             return "redirect:/login";
        }
        emprestimo.setEstudante(estudante);

        if (result.hasErrors()) {
            model.addAttribute("material", emprestimo.getMaterial());
            return "emprestimo/solicitar";
        }

        if (emprestimoService.hasOpenProposal(estudante, emprestimo.getMaterial())) {
            result.rejectValue("material", "error.emprestimo", "Você já tem uma proposta em aberto para este material.");
            model.addAttribute("material", emprestimo.getMaterial());
            return "emprestimo/solicitar";
        }

        emprestimoService.salvar(emprestimo);
        attr.addFlashAttribute("sucesso", "Solicitação de empréstimo enviada com sucesso!");
        return "redirect:/materiais/listar";
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
    public String aprovarEmprestimo(@PathVariable("id") Long id, RedirectAttributes attr) {
        emprestimoService.aprovarEmprestimo(id);
        attr.addFlashAttribute("sucesso", "Empréstimo aprovado com sucesso!");
        return "redirect:/emprestimos/solicitacoes-para-meus-materiais";
    }

    @PostMapping("/recusar/{id}")
    public String recusarEmprestimo(@PathVariable("id") Long id,
                                   @RequestParam(value = "justificativa", required = false) String justificativa,
                                   RedirectAttributes attr) {
        emprestimoService.recusarEmprestimo(id, justificativa);
        attr.addFlashAttribute("sucesso", "Empréstimo recusado com sucesso!");
        return "redirect:/emprestimos/solicitacoes-para-meus-materiais";
    }
}