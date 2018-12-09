package br.com.payments.wallet.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositCash {

	@TargetAggregateIdentifier
	private final String id;
	private final Double amount;
	
}
