package br.ufscar.dc.dsw.controller;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/materiais")
public class MaterialController {

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IEstudanteService estudanteService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("categorias")
    public Categoria[] categorias() {
        return Categoria.values();
    }

    @ModelAttribute("estados")
    public Material.EstadoConservacao[] estados() {
        return Material.EstadoConservacao.values();
    }

    @GetMapping("/listar")
    public String listar(
            @RequestParam(value = "categoria", required = false) Categoria categoria,
            @RequestParam(value = "palavra", required = false) String palavra,
            ModelMap model) {

        List<Material> materiais;
        if (categoria != null) {
            materiais = materialService.buscarDisponiveis(categoria, palavra);
        } else if (palavra != null && !palavra.isBlank()) {
            materiais = materialService.buscarDisponiveis(null, palavra);
        } else {
            materiais = materialService.buscarDisponiveis(null, null);
        }
        model.addAttribute("materiais", materiais);
        return "material/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Material material) {
        return "material/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Material material, BindingResult result,
                         RedirectAttributes attr, Principal principal) {
        if (result.hasErrors()) {
            return "material/cadastro";
        }
        // associa o dono logado
        String email = principal.getName();
        Estudante dono = estudanteService.buscarPorEmail(email);
        material.setEstudante(dono);

        try {
            materialService.salvar(material);
            String msg = messageSource.getMessage("msg.sucesso.material.salvar", null, LocaleContextHolder.getLocale());
            attr.addFlashAttribute("sucesso", msg);
        } catch (Exception e) {
            // exemplo: BusinessException com código em e.getMessage()
            String code = e.getMessage();
            if ("material.titulo.duplicado".equals(code)) {
                result.rejectValue("titulo", "error.material.titulo.duplicado",
                        messageSource.getMessage("error.material.titulo.duplicado", null, LocaleContextHolder.getLocale()));
            } else {
                // mensagem genérica
                result.reject("", messageSource.getMessage("error.geral", null, Locale.getDefault())
);
            }
            return "material/cadastro";
        }
        return "redirect:/materiais/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable Long id, ModelMap model, Principal principal, RedirectAttributes attr) {
        Material material = materialService.buscarPorId(id);
        if (material == null) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.nao.encontrado", null, LocaleContextHolder.getLocale()));
            return "redirect:/materiais/listar";
        }
        // verifica se o dono é o usuário logado
        Estudante dono = estudanteService.buscarPorEmail(principal.getName());
        if (!material.getEstudante().getId().equals(dono.getId())) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.nao.autorizado", null, LocaleContextHolder.getLocale()));
            return "redirect:/materiais/listar";
        }
        model.addAttribute("material", material);
        return "material/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Material material, BindingResult result,
                         RedirectAttributes attr, Principal principal) {
        if (result.hasErrors()) {
            return "material/cadastro";
        }
        // garante dono
        Estudante dono = estudanteService.buscarPorEmail(principal.getName());
        material.setEstudante(dono);

        try {
            materialService.salvar(material);
            String msg = messageSource.getMessage("msg.sucesso.material.editar", null, LocaleContextHolder.getLocale());
            attr.addFlashAttribute("sucesso", msg);
        } catch (Exception e) {
            String code = e.getMessage();
            if ("material.titulo.duplicado".equals(code)) {
                result.rejectValue("titulo", "error.material.titulo.duplicado", 
                messageSource.getMessage("error.material.titulo.duplicado", null, LocaleContextHolder.getLocale()));
            } else {
                result.reject("", messageSource.getMessage("error.geral", null, Locale.getDefault())
);
            }
            return "material/cadastro";
        }
        return "redirect:/materiais/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, ModelMap model, Principal principal, RedirectAttributes attr) {
        Material material = materialService.buscarPorId(id);
        if (material == null) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.nao.encontrado", null, LocaleContextHolder.getLocale()));
            return "redirect:/materiais/listar";
        }
        Estudante dono = estudanteService.buscarPorEmail(principal.getName());
        if (!material.getEstudante().getId().equals(dono.getId())) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.nao.autorizado", null, LocaleContextHolder.getLocale()));
            return "redirect:/materiais/listar";
        }
        try {
            materialService.excluir(id);
            String msg = messageSource.getMessage("msg.sucesso.material.excluir", null, LocaleContextHolder.getLocale());
            attr.addFlashAttribute("sucesso", msg);
        } catch (Exception e) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.vinculado", null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/materiais/listar";
    }

    @GetMapping("/meus-materiais")
    public String meusMateriais(ModelMap model, Principal principal) {
        Estudante dono = estudanteService.buscarPorEmail(principal.getName());
        List<Material> materiais = materialService.buscarPorDono(dono);
        model.addAttribute("materiais", materiais);
        return "material/meus";
    }
}
