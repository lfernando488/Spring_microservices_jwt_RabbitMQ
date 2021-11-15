package com.lfernando488.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lfernando488.crud.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
