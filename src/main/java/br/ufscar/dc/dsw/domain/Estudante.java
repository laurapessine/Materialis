package br.ufscar.dc.dsw.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "Estudante")
public class Estudante extends AbstractEntity<Long> {
	@NotBlank(message = "{NotBlank.estudante.nome}")
	@Size(max = 256, message = "{Size.estudante.nome}")
	@Column(nullable = false, length = 256)
	private String nome;

	@NotBlank(message = "{NotBlank.estudante.email}")
	@Email(message = "{Email.estudante.email}")
	@Size(max = 200, message = "{Size.estudante.email}")
	@Column(nullable = false, unique = true, length = 200)
	private String email;

	@NotBlank(message = "{NotBlank.estudante.senha}")
	@Size(min = 6, max = 60, message = "{Size.estudante.senha}")
	@Column(nullable = false, length = 60)
	private String senha;

	@NotBlank(message = "{NotBlank.estudante.cpf}")
	@Pattern(regexp = "\\d{11}", message = "{Pattern.estudante.cpf}")
	@Column(nullable = false, unique = true, length = 11)
	private String cpf;

	@NotBlank(message = "{NotBlank.estudante.ra}")
	@Pattern(regexp = "\\d{6}", message = "{Pattern.estudante.ra}")
	@Column(nullable = false, unique = true, length = 6)
	private String ra;

	@NotBlank(message = "{NotBlank.estudante.telefone}")
	@Pattern(regexp = "\\d{11}", message = "{Pattern.estudante.telefone}")
	@Column(nullable = false, unique = true, length = 11)
	private String telefone;

	public enum Sexo {
		Feminino,
		Masculino,
		Outro
	}

	@NotNull(message = "{NotNull.estudante.sexo}")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private Sexo sexo;
	
	@NotNull(message = "{NotNull.estudante.nascimento}")
	@Past(message = "{Past.estudante.nascimento}")
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate nascimento;

	@OneToMany(mappedBy = "estudante")
	private List<Material> materiais;

	@OneToMany(mappedBy = "estudante")
	private List<Emprestimo> emprestimosSolicitados;

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRa() {
		return ra;
	}

	public void setRa(String ra) {
		this.ra = ra;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	public List<Material> getMateriais() {
		return materiais;
	}

	public void setMateriais(List<Material> materiais) {
		this.materiais = materiais;
	}

	public List<Emprestimo> getEmprestimosSolicitados() {
		return emprestimosSolicitados;
	}

	public void setEmprestimosSolicitados(List<Emprestimo> emprestimosSolicitados) {
		this.emprestimosSolicitados = emprestimosSolicitados;
	}
}