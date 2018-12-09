package br.com.payments.wallet.events;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletCreated {

	@TargetAggregateIdentifier
	private final String id;
	public final String owner;
}

