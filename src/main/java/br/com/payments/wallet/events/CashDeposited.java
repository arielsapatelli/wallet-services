package br.com.payments.wallet.events;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CashDeposited {

	@TargetAggregateIdentifier
	private final String id;
	private final Double amount;
}

