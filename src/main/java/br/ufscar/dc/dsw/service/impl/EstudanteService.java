package br.ufscar.dc.dsw.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.ufscar.dc.dsw.dao.IEstudanteDAO;
import br.ufscar.dc.dsw.domain.Estudante;
import br.ufscar.dc.dsw.service.spec.IEstudanteService;

@Service
@Transactional(readOnly = false)
public class EstudanteService implements IEstudanteService {
    @Autowired
    IEstudanteDAO dao;

    public void salvar(Estudante estudante) {
        dao.save(estudante);
    }

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

    @Transactional(readOnly = true)
    public boolean estudanteTemMaterial(Long id) {
        return !dao.findById(id.longValue()).getMateriais().isEmpty();
    }

    @Override
    public Estudante buscarPorEmail(String email) {
        return dao.findByEmail(email);
    }
}