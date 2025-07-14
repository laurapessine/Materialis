package br.ufscar.dc.dsw.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.ufscar.dc.dsw.domain.Emprestimo;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.service.spec.IEmprestimoService;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import br.ufscar.dc.dsw.service.spec.IMaterialService;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoRestController {

    @Autowired
    private IEmprestimoService emprestimoService;
    
    @Autowired
    private IEstudanteService estudanteService;

    @Autowired
    private IMaterialService materialService;

    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        List<Emprestimo> lista = emprestimoService.buscarTodos();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/estudantes/{id}")
    public ResponseEntity<List<Emprestimo>> listarPorEstudante(@PathVariable Long id) {
        Estudante estudante = estudanteService.buscarPorId(id);
        if (estudante == null) {
            return ResponseEntity.notFound().build();
        }
        List<Emprestimo> emprestimos = emprestimoService.buscarPorEstudante(estudante);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/materiais/{id}")
    public ResponseEntity<List<Emprestimo>> listarPorMaterial(@PathVariable Long id) {
        Material material = materialService.buscarPorId(id);
        if (material == null) {
            return ResponseEntity.notFound().build();
        }
        List<Emprestimo> emprestimos = emprestimoService.buscarPorMaterial(material);
        return ResponseEntity.ok(emprestimos);
    }
}