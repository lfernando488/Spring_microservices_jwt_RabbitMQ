package com.lfernando488.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lfernando488.crud.DTO.ProdutoDTO;
import com.lfernando488.crud.exceptions.ResourceNotFoundException;
import com.lfernando488.crud.message.ProdutoSendMessage;
import com.lfernando488.crud.models.Produto;
import com.lfernando488.crud.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	
	private final ProdutoRepository repo;
	private final ProdutoSendMessage produtoSendMessage;
	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
		this.repo = produtoRepository;
		this.produtoSendMessage = produtoSendMessage;
	}
	
	public ProdutoDTO create(ProdutoDTO produtoDTO) {
		ProdutoDTO produtoDTORetorno = ProdutoDTO.create(repo.save(Produto.create(produtoDTO)));
		produtoSendMessage.sendMessage(produtoDTORetorno);
		return produtoDTORetorno;
	}
	
	public Page<ProdutoDTO> findAll(Pageable pageable){
		var page = repo.findAll(pageable);
		return page.map(this::convertToProdutoDTO);
	}
	
	public ProdutoDTO convertToProdutoDTO(Produto produto){
		return ProdutoDTO.create(produto);	
	}
	
	public ProdutoDTO findById(Long id){
		var entity = repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Não foram encontrados resultados para o id " + id));
		return ProdutoDTO.create(entity);
	}
	
	public ProdutoDTO update(ProdutoDTO produtoDTO) {
		final Optional<Produto> optionalProduto = repo.findById(produtoDTO.getId());
		if(!optionalProduto.isPresent()) {
			 new ResourceNotFoundException("Não foram encontrados resultados para o id " + produtoDTO.getId());
		}
		return ProdutoDTO.create(repo.save(Produto.create(produtoDTO)));
	}
	
	public void delete(Long id) {
		Produto entity = repo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Não foram encontrados resultados para o id " + id));
		repo.delete(entity);
	}
	
	
}
