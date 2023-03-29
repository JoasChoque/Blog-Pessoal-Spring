package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	//envia as requisições para nossa aplicação
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	//testar a senha criptografada
	@Autowired
	private UsuarioService usuarioService;
	
	//limpa o banco de dados do teste
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L,"Root","root@root.com","rootroot",""));
	}
	
	@Test
	@DisplayName("Cadastrar 1 usuário")
	public void deveCriarUmUsuario() {
		
		//criando o objeto Usuario com os atributos estipulados na model
		HttpEntity<Usuario> corpoRequisicao= new HttpEntity<Usuario>
		(new Usuario(0L,"Paulo Antunes","paulo_antunes@email.com.br","13465278","https://i.imgur.com/JR7kUFU.jpg"));
		
		
		//sintaxe .exchange("endpoint",HttpMethod.verboHttp,objeto da requisição, conteudo esperado)
		ResponseEntity<Usuario> corpoResposta= testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,corpoRequisicao,Usuario.class);
		
		//verificamos se a resposta da requisição acima é a resposta esperada(quarto parametro do .exchange())
		//devolvendo 201 
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		
		//verificando se o nome e o email foram cadastrados no banco de dados
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação de Usuário")
	public void naoDeveDuplicarUsuario() {
		
		//criado um objeto do tipo usuário no banco de dados
		usuarioService.cadastrarUsuario(new Usuario(0L,"Maria da Silva","maria_silva@email.com.br","13465278","https://i.imgur.com/T12NIp9.jpg"));
		
		//criando a requisicao com o cadastro contendo os mesmos dados do usuario cadastrado anteriormente
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(
				0L,"Maria da Silva","maria_silva@email.com.br","13465278","https://i.imgur.com/T12NIp9.jpg"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST,corpoRequisicao,Usuario.class);
		
		//verificamos se a resposta esperada pelo statusCode da resposta é bad request
		assertEquals(HttpStatus.BAD_REQUEST,corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Atualizar um usuário")
	public void deveAtualizarUmUsuario() {
		//Armazena o resultado da inserção de um usuário
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(
				0L,"Juliana Andrews","juliana_andrews@email.com.br","juliana123","https://i.imgur.com/yDRVeK7.jpg"));
		
		//chamando o usuario update com os dados que serão alterados
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
				"Juliana Andrews Ramos","juliana_ramos@email.com.br","juliana123","https://i.imgur.com/yDRVeK7.jpg");
		
		//criando a requisicao com os dados passados no usuarioUpdate
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				//utilizado para logar e poder acessar o sistema por conta do security
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT,corpoRequisicao,Usuario.class);
		
		//Verificar se o status devolvido é o 200 -> 0k
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		
		//confirmar se os dados de nome e email foram alterados no banco de dados
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Listar todos os usuários")
	public void deveMostrarTodosUsuarios() {
		
		//criando novo usuario
		usuarioService.cadastrarUsuario(new Usuario(
				0L,"Sabrina Sanches","sabrina_sanches@email.com.br","sabrina123","https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.cadastrarUsuario(new Usuario(
				0L,"Ricardo Marques","ricardo_marques@email.com.br","ricardo123","https://i.imgur.com/Sk5SjWE.jpg"));
		
		//criando a resposta passando um login e senha para poder autenticar e acessar o endpoint
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				//A requisicão quando o verbo é get, não tem corpo
				.exchange("/usuarios/all", HttpMethod.GET,null,String.class);
		
		//verificando se a respota do protocolo vai ser Ok também
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}
