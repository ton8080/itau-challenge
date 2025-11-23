package com.itau.challenge.bank.infrastructure.persistence.repository;

import com.itau.challenge.bank.infrastructure.persistence.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, String> {

    @Query(
            value = "SELECT * FROM accounts WHERE account_number = :accountNumber FOR UPDATE",
            nativeQuery = true
    )
    Optional<AccountModel> findByAccountNumberForUpdate(String accountNumber);

    @Query(value = "SELECT * FROM accounts WHERE id = :accountId FOR UPDATE", nativeQuery = true)
    Optional<AccountModel> findByIdForUpdate(UUID accountId);
}