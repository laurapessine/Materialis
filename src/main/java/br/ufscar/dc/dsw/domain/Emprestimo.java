package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Emprestimo")
public class Emprestimo extends AbstractEntity<Long> {

    @NotNull(message = "{NotNull.emprestimo.estudante}")
    @ManyToOne
    @JoinColumn(name = "estudante_id", nullable = false)
    private Estudante estudante;

    @NotNull(message = "{NotNull.emprestimo.material}")
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @NotNull(message = "{NotNull.emprestimo.dataSolicitacao}")
    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDate dataSolicitacao;

    @NotNull(message = "{NotNull.emprestimo.dataDevolucaoPrevista}")
    @FutureOrPresent(message = "{FutureOrPresent.emprestimo.dataDevolucaoPrevista}")
    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao_real")
    private LocalDate dataDevolucaoReal;

    @NotBlank(message = "{NotBlank.emprestimo.justificativa}")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    public enum Status {
        ABERTO,       
        APROVADO,     
        RECUSADO,     
        EM_ANDAMENTO, 
        CONCLUIDO     
    }

    @NotNull(message = "{NotNull.emprestimo.status}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @PrePersist
    protected void onCreate() {
        this.dataSolicitacao = LocalDate.now();
        this.status = Status.ABERTO;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}