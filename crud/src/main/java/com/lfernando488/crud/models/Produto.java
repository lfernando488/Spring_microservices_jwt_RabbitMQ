package com.lfernando488.crud.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import com.lfernando488.crud.DTO.ProdutoDTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Produto")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Produto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="nome", nullable = false, length = 255)
	private String nome;
	
	@Column(name="estoque", nullable = false, length = 10)
	private Integer estoque;
	
	@Column(name="preco", nullable = false, length = 10)
	private Double preco;
	
	public static Produto create (ProdutoDTO produtoDTO) {
		return new ModelMapper().map(produtoDTO, Produto.class);
	}
	
}
