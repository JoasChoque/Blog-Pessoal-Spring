package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*",allowedHeaders="*")
public class PostagemController {
	
	@Autowired
	private PostagemRepository postagemRepository;
	
	@Autowired
	private TemaRepository temaRepository;
	
	//Listando todas as postagens
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
	}
	
	//Fazendo busca das postagens por id
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	//Fazendo busca das postagens pelo titulo
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository
				.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	//Cadastrando postagem
	//@Valid valida as configurações da model(modelo de banco de dados)
	//@RequestBody recebe o conteudo(variavel postagem) e insere como parametro no metodo
	//Postagem postagem -> Criando objeto do tipo postagem para usar os dados dela
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		
		if(temaRepository.existsById(postagem.getTema().getId()))
		return ResponseEntity
				//devolve mensagem com o protocolo http(200 ->ok, 400 ->erro etc)
				.status(HttpStatus.CREATED)
				//conteudo a ser inserido no corpo da postagem
				.body(postagemRepository
				//método da JPA repository salvando a postagem em parametro
				.save(postagem));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	//Atualizando postagens
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		if(postagemRepository.existsById(postagem.getId())) {
			
			if(postagemRepository.existsById(postagem.getTema().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(postagemRepository.save(postagem));
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	//Apagando postagens por Id
	//Mapeia as requests http delete enviadas a um endpoint
	//determinando que o método delete vai responder todas as request de delete
	//atraves do id passado pelo @PathVariable
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {//Método void porque depois de excluir, ele não precisa retornar nada
		
		//cria um objeto do tipo optional que armazena o valor retornado pelo id
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		//verifica se o Objeto postagem(da linha acima) está vazio, caso sim
		//Retorna o status de 404 e ignora as demais linhas, caso não,
		//executa o método deleteById(id)
		if(postagem.isEmpty()) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		postagemRepository.deleteById(id);
		
	}
	
}
