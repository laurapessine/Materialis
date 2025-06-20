package br.ufscar.dc.dsw.domain;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "Estudante")

public class Estudante extends AbstractEntity<Long>{
    
    @NotNull(message = "{NotNull.estudante.nome}")
	@Size(max = 256)
	@Column(nullable = false, length = 256)
	private String nome;

	@NotNull(message = "{NotNull.estudante.email}")
	@Size(max = 200)
	@Column(nullable = false,unique = true, length = 200)
	private String email;
    
	@NotNull(message = "{NotNull.estudante.senha}")
    @Size(max = 15)
	@Column(nullable = false, length = 15)
	private String senha;
	
	@NotNull(message = "{NotNull.estudante.cpf}")
    @Size(min = 11, max = 11, message = "{Size.estudante.cpf}")
	@Column(nullable = false, unique = true, length = 11)
	private String cpf;

    @NotNull(message = "{NotNull.estudante.ra}")
    @Size(min = 6, max = 6, message = "{Size.estudante.ra}")
	@Column(nullable = false, unique = true, length = 6)
	private String ra;

    @NotNull(message = "{NotNull.estudante.telefone}")
    @Size(min = 11, max = 11, message = "{Size.estudante.telefone}")
	@Column(nullable = false, unique = true, length = 11)
	private String telefone;

    @NotNull(message = "{NotNull.estudante.sexo}")
    @Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private String sexo;

    @NotNull(message = "{NotNull.estudante.nascimento}")
	@Column(nullable = false, length = 60)
	private String nascimento;

	@OneToMany(mappedBy = "estudante")
	private List<Material> materiais;

    public enum Sexo{
        Feminino,
        Masculino,
        Outro
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
    
    public String getCPF() {
		return cpf;
	}

	public void setCPF(String cpf) {
		this.cpf = cpf;
	}

    public String getRA() {
		return ra;
	}

	public void setRA(String ra) {
		this.ra = ra;
	}

    public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

    public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

    public String getNascimento() {
		return nascimento;
	}

	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}

	public List<Material> getMateriais() {
		return materiais;
	}

	public void setMateriais(List<Material> materiais) {
		this.materiais = materiais;
	}
}