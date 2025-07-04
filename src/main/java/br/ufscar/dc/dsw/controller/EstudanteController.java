package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/estudantes")
@PreAuthorize("hasRole('ADMIN')")
public class EstudanteController {
	@Autowired
	private IEstudanteService service;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Estudante estudante) {
		return "estudante/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("estudantes", service.buscarTodos());
		return "estudante/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Estudante estudante, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "estudante/cadastro";
		}
		try {
			service.salvar(estudante);
			attr.addFlashAttribute("sucess", "Estudante inserido com sucesso.");
		} catch (DataIntegrityViolationException e) {
			result.rejectValue("cpf", "error.estudante", "CPF já cadastrado.");
			return "estudante/cadastro";
		}
		return "redirect:/estudantes/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("estudante", service.buscarPorId(id));
		return "estudante/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Estudante estudante, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "estudante/cadastro";
		}
		try {
			service.salvar(estudante);
			attr.addFlashAttribute("sucess", "Estudante editado com sucesso.");
			return "redirect:/estudantes/listar";
		} catch (DataIntegrityViolationException e) {
			result.rejectValue("cpf", "error.estudante", "CPF já cadastrado.");
			return "redirect:/estudantes/listar";
		}
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		if (service.estudanteTemMaterial(id)) {
			model.addAttribute("fail", "Estudante não excluído. Possui material(s) vinculado(s).");
		} else {
			service.excluir(id);
			model.addAttribute("sucess", "Estudante excluído com sucesso.");
		}
		return listar(model);
	}
}
