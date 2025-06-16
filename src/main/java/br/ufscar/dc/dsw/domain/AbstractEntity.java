package br.ufscar.dc.dsw.domain;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private ID id;
    // getters, setters, equals, hashCode...
}
