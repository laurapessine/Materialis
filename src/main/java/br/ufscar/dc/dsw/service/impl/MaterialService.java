package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IMaterialDAO;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true) // Boa prática: transações são somente leitura por padrão
public class MaterialService implements IMaterialService {

    @Autowired
    private IMaterialDAO dao;

    @Override
    public Material buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public List<Material> buscarTodos() {
        return (List<Material>) dao.findAll(); // Cast para List
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
            return Collections.emptyList(); // Retorna lista vazia se não houver palavra
        }
        return dao.findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(palavra, palavra);
    }

    /**
     * Lógica de busca combinada.
     * Agora, os filtros de categoria e palavra-chave funcionam juntos.
     * Esta lógica delega a complexidade para a camada DAO (assumindo que o método foi criado lá).
     */
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
            return Collections.emptyList(); // Retorna lista vazia para evitar erros
        }
        return dao.findByEstudante(estudante);
    }

    @Override
    @Transactional(readOnly = false) // Habilita a escrita para este método
    public void salvar(Material material) {
        // O código antigo referente a 'fotos' foi removido, pois agora usamos 'imagem'.
        dao.save(material);
    }

    @Override
    @Transactional(readOnly = false) // Habilita a escrita para este método
    public void excluir(Long id) {
        dao.deleteById(id);
    }
}