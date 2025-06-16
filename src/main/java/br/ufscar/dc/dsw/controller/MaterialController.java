package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/materiais")
public class MaterialController {

    @Autowired
    private IMaterialService service;

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        model.addAttribute("materiais", service.buscarTodos());
        return "material/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Material material) {
        return "material/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Material material, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "material/cadastro";
        }
        service.salvar(material);
        attr.addFlashAttribute("sucesso", "Material cadastrado com sucesso.");
        return "redirect:/materiais/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable Long id, ModelMap model) {
        model.addAttribute("material", service.buscarPorId(id));
        return "material/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Material material, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "material/cadastro";
        }
        service.salvar(material);
        attr.addFlashAttribute("sucesso", "Material atualizado com sucesso.");
        return "redirect:/materiais/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, ModelMap model) {
        service.excluir(id);
        model.addAttribute("sucesso", "Material excluído com sucesso.");
        return listar(model);
    }
}
