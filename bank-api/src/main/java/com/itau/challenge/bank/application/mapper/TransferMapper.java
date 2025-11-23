package com.itau.challenge.bank.application.mapper;

import com.itau.challenge.bank.application.dto.TransferResponseDTO;
import com.itau.challenge.bank.domain.entity.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {
    public TransferResponseDTO toDto(Transfer transfer) {
        if (transfer == null) return null;
        return new TransferResponseDTO(
                transfer.isSuccess(),
                transfer.getMessage(),
                transfer.isBacenNotified(),
                transfer.getRemainingBalance()
        );

    }
}
