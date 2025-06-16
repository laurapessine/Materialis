package br.ufscar.dc.dsw.dao;

import br.ufscar.dc.dsw.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMaterialDAO extends JpaRepository<Material, Long> {
    Material findById(long id);
}