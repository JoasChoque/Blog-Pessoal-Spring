package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

//indica que a classe se trata de uma classe de teste
//Configura o teste para usar outra porta que não seja a que a aplicação está utilizando(8080)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)//indica que o ciclo de vida da classe teste sera por classe
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		//Deletando os usuários da memória
		usuarioRepository.deleteAll();
		
		//criando novos usuários para teste
		usuarioRepository.save(new Usuario(0L,"João da Silva","joao@email.com.br","13465278","https://i.imgur.com/FETvs20.jpg"));
		
		usuarioRepository.save(new Usuario(0L,"Manuela da Silva","manuela@email.com.br","13465278","https://i.imgur.com/NtyGneo.jpg"));
		
		usuarioRepository.save(new Usuario(0L,"Adriana da Silva","adriana@email.com.br","13465278","https://i.imgur.com/mB3VM2N.jpg"));
		
		usuarioRepository.save(new Usuario(0L,"Paulo Antunes","paulo@email.com.br","13465278","https://i.imgur.com/JR7kUFU.jpg"));
	}
	
	@Test //Indica que o método executará um teste
	@DisplayName("Retorna 1 usuário") //Mensagem exibida ao invés do nome do método
	public void deveRetornarUmUsuario() {
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br");
		
		//Caso encontre o valor igual ao passado no .equals, o teste será concluido. Caso contrário, vai falhar
		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br"));
	}
	
	//Para que esse teste retorne aprovado, as inserções feitas no método start() devem ser aprovadas também
	@Test
	@DisplayName("Retorna 3 usuários")
	public void deveRetornarTresUsuarios() {
		
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		
		//verifica se o tamanho da lista correspondem com a quantidade de nomes que tem 'Silva'
		assertEquals(3, listaDeUsuarios.size());
		
		//verifica se os usuários da lista foram cadastrados nessa sequencia
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela da Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana da Silva"));
	}
	
	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}
}
