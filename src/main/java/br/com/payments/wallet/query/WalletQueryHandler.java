package br.com.payments.wallet.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import br.com.payments.wallet.query.entity.WalletEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WalletQueryHandler {

	private final WalletQueryRepository walletRepository;
	
	@QueryHandler
	public WalletEntity on(GetWalletById query) {
		return walletRepository.findById(query.getId()).orElse(null);
	}
	
	
}
