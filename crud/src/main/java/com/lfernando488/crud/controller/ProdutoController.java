package com.lfernando488.crud.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lfernando488.crud.DTO.ProdutoDTO;
import com.lfernando488.crud.services.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private final ProdutoService produtoService;
	private final PagedResourcesAssembler<ProdutoDTO> assembler;
	
	public ProdutoController(ProdutoService produtoService, PagedResourcesAssembler<ProdutoDTO> assembler) {
		super();
		this.produtoService = produtoService;
		this.assembler = assembler;
	}
	
	@GetMapping(value = "/find/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
	public ProdutoDTO findById(@PathVariable("id") Long id){
		ProdutoDTO produtoDTO = produtoService.findById(id);
		produtoDTO.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel());
		return produtoDTO;
	}
	
	@GetMapping(value = "/list/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction){
	
			var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
			Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
			Page<ProdutoDTO> produtos = produtoService.findAll(pageable);
			produtos.stream().forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel()));
			PagedModel<EntityModel<ProdutoDTO>> pageModel = assembler.toModel(produtos);
			return new ResponseEntity<> (pageModel, HttpStatus.OK);
	}
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
					consumes = {"application/json", "application/xml", "application/x-yaml"})
	public ProdutoDTO create (@RequestBody ProdutoDTO produtoDTO) {
		ProdutoDTO prodDto = produtoService.create(produtoDTO);
		prodDto.add(linkTo(methodOn(ProdutoController.class).findById(prodDto.getId())).withSelfRel());
		return prodDto;
	}
	
	@PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
			consumes = {"application/json", "application/xml", "application/x-yaml"})
	public ProdutoDTO update (@RequestBody ProdutoDTO produtoDTO) {
		ProdutoDTO prodDto = produtoService.update(produtoDTO);
		prodDto.add(linkTo(methodOn(ProdutoController.class).findById(produtoDTO.getId())).withSelfRel());
		return prodDto;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete (@PathVariable("id") Long id){
		produtoService.delete(id);
		return ResponseEntity.ok().build();
	}
	
}
