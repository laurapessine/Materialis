package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;

@RestController
@RequestMapping("/api/estudantes")
public class EstudanteRestController {

    @Autowired
    private IEstudanteService estudanteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<Estudante>> listarTodos() {
        List<Estudante> estudantes = estudanteService.buscarTodos();
        if (estudantes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estudantes);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Estudante> lerPorId(@PathVariable Long id) {
        Estudante estudante = estudanteService.buscarPorId(id);
        if (estudante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudante);
    }
    @PostMapping
    public ResponseEntity<Estudante> criar(@RequestBody Estudante estudante) {
        if (estudante.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }
        estudante.setSenha(passwordEncoder.encode(estudante.getSenha()));
        estudanteService.salvar(estudante);
        return ResponseEntity.status(HttpStatus.CREATED).body(estudante);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Estudante> atualizar(@PathVariable Long id, @RequestBody Estudante estudanteDados) {
        Estudante estudanteExistente = estudanteService.buscarPorId(id);
        if (estudanteExistente == null) {
            return ResponseEntity.notFound().build();
        }
        estudanteExistente.setNome(estudanteDados.getNome());
        estudanteExistente.setEmail(estudanteDados.getEmail());
        estudanteExistente.setCpf(estudanteDados.getCpf());
        if (estudanteDados.getSenha() != null && !estudanteDados.getSenha().isEmpty()) {
            estudanteExistente.setSenha(passwordEncoder.encode(estudanteDados.getSenha()));
        }

        estudanteService.salvar(estudanteExistente);
        return ResponseEntity.ok(estudanteExistente);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (estudanteService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (estudanteService.estudanteTemMaterial(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        estudanteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}