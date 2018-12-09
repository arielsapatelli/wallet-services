package br.com.payments.wallet.query;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetWalletById {

	private String id;
}
