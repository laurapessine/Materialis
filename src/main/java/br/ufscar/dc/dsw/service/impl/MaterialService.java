package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IMaterialDAO;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.service.spec.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class MaterialService implements IMaterialService {

    @Autowired
    private IMaterialDAO dao;

    @Override
    public Material buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public List<Material> buscarTodos() {
        return dao.findAll();
    }

    @Override
    public List<Material> buscarPorCategoria(Categoria categoria) {
        return categoria == null ? buscarTodos() : dao.findByCategoria(categoria);
    }

    @Override
    public List<Material> buscarPorPalavraChave(String palavra) {
        return (palavra == null || palavra.isBlank()) ? buscarTodos()
                : dao.findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(palavra, palavra);
    }

    @Override
    public List<Material> buscarDisponiveis(Categoria categoria, String palavra) {
        List<Material> lista = buscarTodos();
        if (categoria != null) {
            lista = dao.findByCategoria(categoria);
        }
        if (palavra != null && !palavra.isBlank()) {
            lista = dao.findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(palavra, palavra);
        }
        return lista;
    }

    @Override
    public List<Material> buscarPorDono(Estudante estudante) {
        return estudante == null ? List.of() : dao.findByEstudante(estudante);
    }

    @Override
    public void salvar(Material material) {
        dao.save(material);
    }

    @Override
    public void excluir(Long id) {
        dao.deleteById(id);
    }
}
