package br.com.payments.wallet.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import br.com.payments.wallet.commands.CreateWallet;
import br.com.payments.wallet.commands.DepositCash;
import br.com.payments.wallet.commands.WithdrawCash;
import br.com.payments.wallet.events.CashDeposited;
import br.com.payments.wallet.events.CashWithdrawn;
import br.com.payments.wallet.events.WalletCreated;
import br.com.payments.wallet.exception.InsufficientCashException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Aggregate
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

	@AggregateIdentifier
	private String id;
	private double balance;
	private String owner;

	@CommandHandler
	public Wallet(CreateWallet create) {
		AggregateLifecycle.apply(
				WalletCreated.builder()
				.id(create.getId())
				.owner(create.getOwner())
				.build());
	}

	@CommandHandler
	public Double on(WithdrawCash withdraw) throws InsufficientCashException {
		Assert.isTrue(withdraw.getAmount() > 0.0, "amount must be greater then 0.");
		Assert.notNull(withdraw.getAmount(), "amount must not be null.");

		if (balance - withdraw.getAmount() < 0) {
			throw new InsufficientCashException("you doesnt have sufficient funds.");
		} else {
			AggregateLifecycle.apply(CashWithdrawn.builder().id(withdraw.getId()).amount(withdraw.getAmount()).build());
			return withdraw.getAmount();
		}
	}

	@CommandHandler
	public Double on(DepositCash deposit) {
		Assert.isTrue(deposit.getAmount() > 0.0, "amount must be greater then 0.");
		Assert.notNull(deposit.getAmount(), "amount must not be null.");
		AggregateLifecycle.apply(CashDeposited.builder().id(deposit.getId()).amount(deposit.getAmount()).build());
		return deposit.getAmount();
	}

	@EventSourcingHandler
	public void on(WalletCreated created) {
		this.id = created.getId();
		this.owner = created.owner;
	}

	@EventSourcingHandler
	public void on(CashWithdrawn withdrawn) {
		this.balance = (this.balance)- withdrawn.getAmount();
	}

	@EventSourcingHandler
	public void on(CashDeposited deposited) {
		this.balance = (this.balance) + deposited.getAmount() ;
	}

}
