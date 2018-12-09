package br.com.payments.wallet.projection;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.payments.wallet.query.GetWalletById;
import br.com.payments.wallet.query.WalletQueryRepository;
import br.com.payments.wallet.query.entity.WalletEntity;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("wallet")
@AllArgsConstructor
public class WalletProjection {

	private final QueryGateway queryGateway;

	private final WalletQueryRepository walletRepository;

	@GetMapping("/{id}")
	public CompletableFuture<WalletEntity> getWallet(@PathVariable("id") String id) {
		return queryGateway.query(GetWalletById.builder().id(id).build(), ResponseTypes.instanceOf(WalletEntity.class));

	}

	@GetMapping(value = "/{id}/balance", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
	public Flux<WalletEntity> getBalance(@PathVariable("id") String id) {

		Flux<WalletEntity> eventFlux = Flux
				.fromStream(Stream.generate(() -> walletRepository.findById(id).get()));
		Flux<Long> durationFlux = Flux.interval(Duration.ofMillis(100l));
		return Flux.zip(eventFlux, durationFlux).map(Tuple2::getT1);
	}

}
