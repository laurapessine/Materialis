package br.ufscar.dc.dsw.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/materiais")
public class MaterialController {

    @Autowired
    private IMaterialService service;

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
        if (categoria != null && categoria.toString().isBlank()) {
            materiais = service.buscarPorCategoria(categoria);
        } else if (palavra != null && !palavra.isBlank()) {
            materiais = service.buscarPorPalavraChave(palavra);
        } else {
            materiais = service.buscarTodos();
        }
        model.addAttribute("materiais", materiais);
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
        attr.addFlashAttribute("sucesso", "{msg.sucesso.material.salvar}");
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
        attr.addFlashAttribute("sucesso", "{msg.sucesso.material.editar}");
        return "redirect:/materiais/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, ModelMap model) {
        service.excluir(id);
        model.addAttribute("sucesso", "{msg.sucesso.material.excluir}");
        return listar(null, null, model);
    }
}