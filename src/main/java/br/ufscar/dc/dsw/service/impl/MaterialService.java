package br.ufscar.dc.dsw.service.impl;

import br.ufscar.dc.dsw.dao.IMaterialDAO;
import br.ufscar.dc.dsw.domain.Material;
import br.ufscar.dc.dsw.domain.Material.Categoria;
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
        try {
            Categoria cat = Categoria.valueOf(categoria.toString().toUpperCase());
            return dao.findByCategoria(cat);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Override
    public List<Material> buscarPorPalavraChave(String palavra) {
        return dao.findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(palavra, palavra);
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