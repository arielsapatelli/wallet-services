package br.com.payments.wallet.projection;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import br.com.payments.wallet.events.CashDeposited;
import br.com.payments.wallet.events.CashWithdrawn;
import br.com.payments.wallet.events.WalletCreated;
import br.com.payments.wallet.query.WalletQueryRepository;
import br.com.payments.wallet.query.entity.WalletEntity;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Component
@AllArgsConstructor
@Log
public class WalletEventHandler {

	private final WalletQueryRepository walletRepository;
	
	
	@EventHandler
	public void on(WalletCreated created) {
	
		walletRepository.save(new WalletEntity(created.getId(),0.0,created.getOwner()));
		log.info("Wallet {"+ created +"} created in query database.");
	}
	
	@EventHandler
	public void on(CashWithdrawn withdrawn) {
	
		Optional<WalletEntity> wallet = walletRepository.findById(withdrawn.getId());
		wallet.ifPresent(w->{
			w.setBalance(w.getBalance() - withdrawn.getAmount());
			walletRepository.save(w);
		});

		log.info("Wallet Cash {"+ withdrawn +"}  Withdrawn in query database.");
	}
	
	
	@EventHandler
	public void on(CashDeposited deposited) {
	
		Optional<WalletEntity> wallet = walletRepository.findById(deposited.getId());
		wallet.ifPresent(w->{
			w.setBalance(w.getBalance() + deposited.getAmount());
			walletRepository.save(w);
		});

		log.info("Wallet Cash {"+ deposited +"}  Deposited in query database.");
	}
	
}
