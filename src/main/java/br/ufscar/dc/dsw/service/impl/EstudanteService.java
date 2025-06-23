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
    public Estudante buscarPorId(Long id) {
        return dao.findById(id).orElse(null);
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
    public List<Estudante> buscarTodos() {
        return dao.findAll();
    }

    @Override
    public void salvar(Estudante estudante) {
        dao.save(estudante);
    }

    @Override
    public void excluir(Long id) {
        dao.deleteById(id);
    }

    @Override
    public boolean estudanteTemMaterial(Long id) {
        // implementar: verifica se há materiais vinculados a este estudante.
        // p.ex:
        // return materialDAO.existsByEstudanteId(id);
        // ou trazer lista e checar size>0.
        return false; // ajuste conforme lógica
    }
}