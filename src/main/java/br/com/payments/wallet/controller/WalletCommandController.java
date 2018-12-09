package br.com.payments.wallet.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.payments.wallet.commands.CreateWallet;
import br.com.payments.wallet.commands.DepositCash;
import br.com.payments.wallet.commands.WithdrawCash;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController()
@RequestMapping("wallet")
public class WalletCommandController {

	private final CommandGateway commandGateway;

	@PostMapping
	public CompletableFuture<String> create(@RequestBody CreateRequest creating) {
		return commandGateway.send(CreateWallet.builder().id(UUID.randomUUID().toString()).owner(creating.getOwner()).build());
	}

	@PostMapping(value = { "/{id}/deposit" })
	public CompletableFuture<Double> deposit(@PathVariable("id") String id, @RequestBody DepositRequest req)
			throws InterruptedException, ExecutionException, TimeoutException {
		return commandGateway.send(DepositCash.builder().id(id).amount(req.getAmount()).build());
	}

	@PostMapping(value = { "/{id}/withdraw" })
	public CompletableFuture<Double> withdraw(@PathVariable("id") String id, @RequestBody WithdrawRequest req)
			throws InterruptedException, ExecutionException, TimeoutException {
		return commandGateway.send(WithdrawCash.builder().id(id).amount(req.getAmount()).build());
	}
}
