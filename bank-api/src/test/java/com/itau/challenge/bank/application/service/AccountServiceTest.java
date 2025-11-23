package com.itau.challenge.bank.application.service;

import com.itau.challenge.bank.application.dto.CheckAccountResponseDTO;
import com.itau.challenge.bank.application.mapper.AccountAppMapper;
import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.usecase.CheckAccountUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private CheckAccountUseCase checkAccountUseCase;

    @Mock
    private AccountAppMapper accountAppMapper;

    @InjectMocks
    private AccountService service;

    @Test
    void returnsDtoWhenUseCaseReturnsDomain() {
        UUID accountId = UUID.randomUUID();
        Account domain = mock(Account.class);
        CheckAccountResponseDTO dto = mock(CheckAccountResponseDTO.class);

        when(checkAccountUseCase.checkAccount(accountId.toString())).thenReturn(domain);
        when(accountAppMapper.toDto(domain)).thenReturn(dto);

        CheckAccountResponseDTO result = service.checkAccount(accountId);

        assertSame(dto, result);
        verify(checkAccountUseCase).checkAccount(accountId.toString());
        verify(accountAppMapper).toDto(domain);
    }

    @Test
    void throwsNullPointerWhenAccountIdIsNull() {
        assertThrows(NullPointerException.class, () -> service.checkAccount(null));
    }
}
