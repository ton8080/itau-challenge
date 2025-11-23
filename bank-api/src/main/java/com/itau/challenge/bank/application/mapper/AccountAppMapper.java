package com.itau.challenge.bank.application.mapper;

import com.itau.challenge.bank.application.dto.CheckAccountResponseDTO;
import com.itau.challenge.bank.domain.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountAppMapper {


    public CheckAccountResponseDTO toDto(Account account) {
        return new CheckAccountResponseDTO(
                account.getAccountNumber(),
                account.isActiveAccount(),
                account.getBalance(),
                account.getDailyTransferred(),
                account.getCustomerName());
    }
}
