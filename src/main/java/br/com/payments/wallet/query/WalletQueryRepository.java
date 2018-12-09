package br.com.payments.wallet.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.payments.wallet.query.entity.WalletEntity;

@Repository
public interface WalletQueryRepository extends JpaRepository<WalletEntity, String> {
}
