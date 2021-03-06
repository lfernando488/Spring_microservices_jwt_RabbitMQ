package com.lfernando488.pagamento.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.lfernando488.pagamento.DTO.ProdutoDTO;
import com.lfernando488.pagamento.entity.Produto;
import com.lfernando488.pagamento.repository.ProdutoRepository;

@Component
public class ProdutoReceiveMessage {

	private final ProdutoRepository produtoRepository;
	
	@Autowired
	public ProdutoReceiveMessage(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	@RabbitListener(queues = {"${crud.rabbitmq.queue}"})
	public void receive (@Payload ProdutoDTO produtoDTO) {
		produtoRepository.save(Produto.create(produtoDTO));
	}
	
}
