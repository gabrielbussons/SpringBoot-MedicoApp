package com.medicoapp.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="medico")

public class MedicoModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotNull(message = "O campo nome não pode ser nulo.")
	private String nome;
	
	@NotNull(message = "O campo dataNascimento não pode ser nulo.")
	private Date dataNascimento;
	
	private boolean ativo;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable
    private Set<EspecialidadeModel> especialidades;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Set<EspecialidadeModel> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(Set<EspecialidadeModel> especialidades) {
		this.especialidades = especialidades;
	}

	
	
}
