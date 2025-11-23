package com.itau.challenge.bank.domain.usecase;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.entity.Transfer;
import com.itau.challenge.bank.domain.entity.QueueMessage;
import com.itau.challenge.bank.domain.exceptions.AccountNotFoundException;
import com.itau.challenge.bank.domain.exceptions.InvalidAmountException;
import com.itau.challenge.bank.domain.exceptions.ValidationException;
import com.itau.challenge.bank.domain.provider.*;
import com.itau.challenge.bank.domain.utils.validator.*;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferUseCase {

    private final GetCustomerByIdProvider getCustomerByIdProvider;
    private final SaveAccountProvider saveAccountProvider;
    private final FindAccountByIdProvider findAccountByIdProvider;
    private final FindByAccountNumberProvider findByAccountNumberProvider;
    private final BacenNotifierProvider bacenNotifier;
    private final DailyLimitProvider dailyLimitProvider;


    public TransferUseCase(GetCustomerByIdProvider getCustomerByIdProvider,
                           SaveAccountProvider saveAccountProvider,
                           FindAccountByIdProvider findAccountByIdProvider,
                           FindByAccountNumberProvider findByAccountNumberProvider,
                           BacenNotifierProvider bacenNotifier, DailyLimitProvider dailyLimitProvider) {

        this.getCustomerByIdProvider = getCustomerByIdProvider;
        this.saveAccountProvider = saveAccountProvider;
        this.findAccountByIdProvider = findAccountByIdProvider;
        this.findByAccountNumberProvider = findByAccountNumberProvider;
        this.bacenNotifier = bacenNotifier;
        this.dailyLimitProvider = dailyLimitProvider;
    }

    private TransferValidator buildValidatorChain() {
        TransferValidator active = new ActiveValidator();
        TransferValidator daily = new DailyLimitValidator(dailyLimitProvider.getDailyLimit());
        TransferValidator balance = new BalanceValidator();
        active.setNext(daily).setNext(balance);
        return active;
    }

    public Transfer execute(String accountId, BigDecimal amount, String targetAccountNumber)
            throws AccountNotFoundException {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount");
        }

        var customer = getCustomerByIdProvider.getCustomerById(accountId);
        if (customer == null) {
            throw new AccountNotFoundException(accountId);
        }

        Account from = findAccountByIdProvider.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        from.setCustomerName(customer.getCustomerName());
        from.setActiveAccount(customer.isActiveAccount());

        TransferValidator chain = buildValidatorChain();
        ValidationResult vr = chain.validate(from, amount);
        if (!vr.isValid()) {
            throw new ValidationException(vr.getMessage(), from.getBalance());
        }

        Account to = findByAccountNumberProvider.findByAccountNumberForUpdate(targetAccountNumber)
                .orElseThrow(() -> new ValidationException("Target account not found", null));

        Account updatedFrom = from.debit(amount);
        Account updatedTo  = to.credit(amount);

        saveAccountProvider.save(updatedFrom);
        saveAccountProvider.save(updatedTo);

        QueueMessage message = new QueueMessage(
                UUID.randomUUID().toString(),
                from.getAccountNumber(),
                targetAccountNumber,
                amount
        );

        boolean notified = bacenNotifier.notifyBacen(message);

        String msg = notified ?
                "Transfer completed" :
                "Transfer completed, bacen enqueued for retry";

        return new Transfer(true, msg, notified, updatedFrom.getBalance());
    }

}
