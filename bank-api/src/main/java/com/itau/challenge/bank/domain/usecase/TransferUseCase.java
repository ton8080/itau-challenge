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
        TransferValidator amount = new AmountValidator();
        TransferValidator customer = new CustomerValidator();
        TransferValidator active = new ActiveValidator();
        TransferValidator daily = new DailyLimitValidator(dailyLimitProvider.getDailyLimit());
        TransferValidator balance = new BalanceValidator();

        amount.setNext(customer).setNext(active).setNext(daily).setNext(balance);
        return amount;
    }

    public Transfer execute(String accountId, BigDecimal amount, String targetAccountNumber)
            throws AccountNotFoundException {

        Account from = findAccountByIdProvider.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        var customer = getCustomerByIdProvider.getCustomerById(accountId);
        from.setCustomerName(customer != null ? customer.getCustomerName() : null);
        from.setActiveAccount(customer != null && customer.isActiveAccount());

        TransferValidator chain = buildValidatorChain();
        ValidationResult vr = chain.validate(from, amount);
        if (!vr.isValid()) {
            String msg = vr.getMessage();
            if ("Invalid amount".equals(msg)) {
                throw new InvalidAmountException("Invalid amount");
            }
            if ("Customer not found".equals(msg)) {
                throw new AccountNotFoundException(accountId);
            }
            throw new ValidationException(msg, from.getBalance());
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
