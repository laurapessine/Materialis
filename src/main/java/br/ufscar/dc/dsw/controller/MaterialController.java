package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * R05: Lista os materiais com filtro por categoria e/ou palavra-chave.
     * A lógica foi simplificada para passar os parâmetros diretamente para a camada de serviço.
     */
    @GetMapping("/listar")
    public String listar(@RequestParam(value = "categoria", required = false) Categoria categoria, @RequestParam(value = "palavra", required = false) String palavra, ModelMap model, Principal principal) {
        List<Material> materiais = materialService.buscarDisponiveis(categoria, palavra);
        // R06: Garante que o usuário logado não possa solicitar o próprio material.
        if (principal != null) {
            Estudante usuarioLogado = estudanteService.buscarPorEmail(principal.getName());
            model.addAttribute("usuarioLogadoId", usuarioLogado.getId());
        }
        model.addAttribute("materiais", materiais);
        return "material/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Material material) {
        return "material/cadastro";
    }

    /**
     * R02 & R04: Salva um novo material com upload de imagem.
     * O redirecionamento foi alterado para /meus-materiais para melhor UX.
     */
    @PostMapping("/salvar")
    public String salvar(@Valid Material material, BindingResult result, @RequestParam("imagemFile") MultipartFile file, RedirectAttributes attr, Principal principal) throws IOException {
        if (result.hasErrors()) {
            return "material/cadastro";
        }
        if (file != null && !file.isEmpty()) {
            material.setNomeImagem(StringUtils.cleanPath(file.getOriginalFilename()));
            material.setTipoImagem(file.getContentType());
            material.setImagem(file.getBytes());
        }
        String email = principal.getName();
        Estudante dono = estudanteService.buscarPorEmail(email);
        material.setEstudante(dono);
        try {
            materialService.salvar(material);
            String msg = messageSource.getMessage("msg.sucesso.material.salvar", null, LocaleContextHolder.getLocale());
            attr.addFlashAttribute("sucesso", msg);
        } catch (DataIntegrityViolationException e) { // Este catch é mais específico para erros de banco de dados, como constraints de unicidade.
            result.reject("error.geral", messageSource.getMessage("error.geral", null, LocaleContextHolder.getLocale()));
            return "material/cadastro";
        }
        return "redirect:/materiais/meus-materiais";
    }

    @GetMapping("/imagem/{id}")
    public void exibirImagem(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        Material material = materialService.buscarPorId(id);
        if (material != null && material.getImagem() != null) {
            response.setContentType(material.getTipoImagem());
            response.getOutputStream().write(material.getImagem());
            response.getOutputStream().close();
        }
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable Long id, ModelMap model, Principal principal, RedirectAttributes attr) {
        Material material = materialService.buscarPorId(id);
        if (material == null) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.nao.encontrado", null, LocaleContextHolder.getLocale()));
            return "redirect:/materiais/meus-materiais";
        }
        Estudante dono = estudanteService.buscarPorEmail(principal.getName());
        if (!material.getEstudante().getId().equals(dono.getId())) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.nao.autorizado", null, LocaleContextHolder.getLocale()));
            return "redirect:/materiais/listar";
        }
        model.addAttribute("material", material);
        return "material/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid Material material, BindingResult result, @RequestParam("imagemFile") MultipartFile file, RedirectAttributes attr, Principal principal) throws IOException {
        if (result.hasErrors()) {
            return "material/cadastro";
        }
        if (file != null && !file.isEmpty()) {
            material.setNomeImagem(StringUtils.cleanPath(file.getOriginalFilename()));
            material.setTipoImagem(file.getContentType());
            material.setImagem(file.getBytes());
        } else {
            // Mantém a imagem antiga se nenhuma nova for enviada.
            Material materialExistente = materialService.buscarPorId(material.getId());
            if (materialExistente != null) {
                material.setImagem(materialExistente.getImagem());
                material.setNomeImagem(materialExistente.getNomeImagem());
                material.setTipoImagem(materialExistente.getTipoImagem());
            }
        }
        Estudante dono = estudanteService.buscarPorEmail(principal.getName());
        material.setEstudante(dono);
        try {
            materialService.salvar(material);
            String msg = messageSource.getMessage("msg.sucesso.material.editar", null, LocaleContextHolder.getLocale());
            attr.addFlashAttribute("sucesso", msg);
        } catch (DataIntegrityViolationException e) {
             result.reject("error.geral", messageSource.getMessage("error.geral", null, LocaleContextHolder.getLocale()));
            return "material/cadastro";
        }
        return "redirect:/materiais/meus-materiais";
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, Principal principal, RedirectAttributes attr) {
        Material material = materialService.buscarPorId(id);
        if (material == null) {
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.nao.encontrado", null, LocaleContextHolder.getLocale()));
            return "redirect:/materiais/meus-materiais";
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
        } catch (DataIntegrityViolationException e) {
            // Mensagem para o caso de o material estar vinculado a um empréstimo, por exemplo.
            attr.addFlashAttribute("fail", messageSource.getMessage("error.material.vinculado", null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/materiais/meus-materiais";
    }

    /**
     * R07: Lista os materiais que o estudante logado colocou para empréstimo.
     */
    @GetMapping("/meus-materiais")
    public String meusMateriais(ModelMap model, Principal principal) {
        Estudante dono = estudanteService.buscarPorEmail(principal.getName());
        List<Material> materiais = materialService.buscarPorDono(dono);
        model.addAttribute("materiais", materiais);
        return "material/meus";
    }
}