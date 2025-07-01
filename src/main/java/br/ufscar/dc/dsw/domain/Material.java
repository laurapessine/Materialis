package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "Material")
public class Material extends AbstractEntity<Long> {
    @NotBlank(message = "{NotBlank.material.titulo}")
    @Column(nullable = false, length = 255)
    private String titulo;

    @NotBlank(message = "{NotBlank.material.descricao}")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    public enum Categoria {
        PAPELARIA,
        LIVROS,
        ELETRONICOS
    }

    @NotNull(message = "{NotNull.material.categoria}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    public enum EstadoConservacao {
        NOVO,
        EXCELENTE,
        BOM,
        REGULAR,
        RUIM
    }

    @NotNull(message = "{NotNull.material.estadoConservacao}")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_conservacao", nullable = false)
    private EstadoConservacao estadoConservacao;

    @Column(columnDefinition = "JSON")
    private String fotos;

    @NotBlank(message = "{NotBlank.material.localRetirada}")
    @Column(name = "local_retirada", nullable = false, length = 255)
    private String localRetirada;

    @ManyToOne
    @JoinColumn(name = "estudante_id", nullable = false)
    private Estudante estudante;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public EstadoConservacao getEstadoConservacao() {
        return estadoConservacao;
    }

    public void setEstadoConservacao(EstadoConservacao estadoConservacao) {
        this.estadoConservacao = estadoConservacao;
    }

    public String getFotos() {
        return fotos;
    }

    public void setFotos(String fotos) {
        this.fotos = fotos;
    }

    public String getLocalRetirada() {
        return localRetirada;
    }

    public void setLocalRetirada(String localRetirada) {
        this.localRetirada = localRetirada;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}