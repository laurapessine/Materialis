package br.ufscar.dc.dsw.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import br.ufscar.dc.dsw.service.spec.IMaterialService;

@RestController
@RequestMapping("/api/materiais")
public class MaterialRestController {

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IEstudanteService estudanteService;

    @GetMapping
    public ResponseEntity<List<Material>> listarTodos() {
        List<Material> materiais = materialService.buscarTodos();
        if (materiais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(materiais);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Material> buscarPorId(@PathVariable Long id) {
        Material material = materialService.buscarPorId(id);
        return material != null ? ResponseEntity.ok(material) : ResponseEntity.notFound().build();
    }
    @GetMapping("/estudantes/{id}")
    public ResponseEntity<List<Material>> listarPorEstudante(@PathVariable Long id) {
        Estudante estudante = estudanteService.buscarPorId(id);
        if (estudante == null) {
            return ResponseEntity.notFound().build();
        }
        List<Material> materiais = materialService.buscarPorDono(estudante);
        return ResponseEntity.ok(materiais);
    }
    @PostMapping("/estudantes/{idEstudante}")
    public ResponseEntity<Material> criarMaterial(@PathVariable Long idEstudante, @RequestBody Material material) {
        Estudante estudante = estudanteService.buscarPorId(idEstudante);
        if (estudante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        material.setEstudante(estudante);
        materialService.salvar(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(material);
    }
}