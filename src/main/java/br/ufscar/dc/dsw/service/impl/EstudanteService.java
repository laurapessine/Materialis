package br.ufscar.dc.dsw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.ufscar.dc.dsw.dao.IEstudanteDAO;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;
import java.util.List;

@Service
@Transactional
public class EstudanteService implements IEstudanteService {

    @Autowired
    private IEstudanteDAO dao;

    @Override
    public void salvar(Estudante estudante) {
        dao.save(estudante);
    }

    @Override
    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Override
    public Estudante buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public List<Estudante> buscarTodos() {
        return dao.findAll();
    }

    @Override
    public Estudante buscarPorCPF(String cpf) {
        return dao.findByCpf(cpf);
    }

    @Override
    public Estudante buscarPorEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estudanteTemMaterial(Long id) {
        return dao.findById(id)
                 .map(e -> !e.getMateriais().isEmpty())
                 .orElse(false);
    }
}