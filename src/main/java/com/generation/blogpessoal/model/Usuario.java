package com.generation.blogpessoal.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tb_usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@NotNull(message = "O campo nome é obrigatório")
	private String nome;
	
	@NotNull(message="O campo usuario é obrigatório")
	@Email(message = "O campo usuário deve conter um e-mail válido!")
	private String usuario;
	
	@NotBlank
	@Size(min = 8, message="A senha deve ter no mínimo 8 caracteres")
	private String senha;
	
	@Size(max=5000,message="O link da foto não pode ter mais de 5000 caracteres")
	private String foto;
	
	@OneToMany(mappedBy="usuario", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("usuario")
	private List<Postagem> postagem;

	/*Construtores*/
	public Usuario(Long id,String nome, String usuario,String senha,String foto) {
		this.id=id;
		this.nome=nome;
		this.usuario=usuario;
		this.senha=senha;
		this.foto=foto;
	}
	
	public Usuario() {}
	
	/*Getters and Setters*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	
	
}
