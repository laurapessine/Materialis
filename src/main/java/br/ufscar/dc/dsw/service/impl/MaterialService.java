package br.ufscar.dc.dsw.service.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.ufscar.dc.dsw.dao.IMaterialDAO;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.service.spec.IMaterialService;

@Service
@Transactional(readOnly = true)
public class MaterialService implements IMaterialService {
    @Autowired
    private IMaterialDAO dao;

    @Override
    public Material buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public List<Material> buscarTodos() {
        return (List<Material>) dao.findAll();
    }

    @Override
    public List<Material> buscarPorCategoria(Categoria categoria) {
        if (categoria == null) {
            return buscarTodos();
        }
        return dao.findByCategoria(categoria);
    }

    @Override
    public List<Material> buscarPorPalavraChave(String palavra) {
        if (palavra == null || palavra.isBlank()) {
            return Collections.emptyList(); 
        }
        return dao.findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(palavra, palavra);
    }

    @Override
    public List<Material> buscarDisponiveis(Categoria categoria, String palavra) {
        boolean hasPalavra = palavra != null && !palavra.isBlank();
        boolean hasCategoria = categoria != null;
        if (hasPalavra && hasCategoria) {
            // Se ambos os filtros são fornecidos, busca combinada
            return dao.findByCategoriaAndTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(categoria, palavra, palavra);
        } else if (hasCategoria) {
            // Apenas filtro por categoria
            return dao.findByCategoria(categoria);
        } else if (hasPalavra) {
            // Apenas filtro por palavra-chave
            return dao.findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(palavra, palavra);
        } else {
            // Nenhum filtro, retorna todos
            return buscarTodos();
        }
    }

    @Override
    public List<Material> buscarPorDono(Estudante estudante) {
        if (estudante == null) {
            return Collections.emptyList();
        }
        return dao.findByEstudante(estudante);
    }

    @Override
    @Transactional(readOnly = false) 
    public void salvar(Material material) {
        dao.save(material);
    }

    @Override
    @Transactional(readOnly = false) 
    public void excluir(Long id) {
        dao.deleteById(id);
    }
}