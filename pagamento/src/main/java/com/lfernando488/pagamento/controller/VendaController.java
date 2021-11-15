package com.lfernando488.pagamento.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lfernando488.pagamento.DTO.VendaDTO;
import com.lfernando488.pagamento.services.VendaService;

@RestController
@RequestMapping("/venda")
public class VendaController {

	@Autowired
	private final VendaService VendaService;
	private final PagedResourcesAssembler<VendaDTO> assembler;
	
	public VendaController(VendaService VendaService, PagedResourcesAssembler<VendaDTO> assembler) {
		super();
		this.VendaService = VendaService;
		this.assembler = assembler;
	}
	
	@GetMapping(value = "/find/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
	public VendaDTO findById(@PathVariable("id") Long id){
		VendaDTO VendaDTO = VendaService.findById(id);
		VendaDTO.add(linkTo(methodOn(VendaController.class).findById(id)).withSelfRel());
		return VendaDTO;
	}
	
	@GetMapping(value = "/list/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction){
	
			var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
			Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "data"));
			Page<VendaDTO> Vendas = VendaService.findAll(pageable);
			Vendas.stream().forEach(p -> p.add(linkTo(methodOn(VendaController.class).findById(p.getId())).withSelfRel()));
			PagedModel<EntityModel<VendaDTO>> pageModel = assembler.toModel(Vendas);
			return new ResponseEntity<> (pageModel, HttpStatus.OK);
	}
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
					consumes = {"application/json", "application/xml", "application/x-yaml"})
	public VendaDTO create (@RequestBody VendaDTO VendaDTO) {
		VendaDTO prodDto = VendaService.create(VendaDTO);
		prodDto.add(linkTo(methodOn(VendaController.class).findById(prodDto.getId())).withSelfRel());
		return prodDto;
	}
}
