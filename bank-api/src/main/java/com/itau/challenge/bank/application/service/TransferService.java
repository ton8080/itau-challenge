package com.itau.challenge.bank.application.service;

import com.itau.challenge.bank.application.dto.TransferRequestDTO;
import com.itau.challenge.bank.application.dto.TransferResponseDTO;
import com.itau.challenge.bank.application.mapper.TransferMapper;
import com.itau.challenge.bank.domain.usecase.TransferUseCase;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class TransferService {


    private final TransferUseCase transferUseCase;
    private final TransferMapper transferMapper;

    public TransferService(TransferUseCase transferUseCase, TransferMapper transferMapper) {
        this.transferUseCase = transferUseCase;
        this.transferMapper = transferMapper;
    }

    public TransferResponseDTO transfer(TransferRequestDTO request) {
        return Stream.of(request)
                .map(r -> transferUseCase.execute(r.fromAccountId(), r.amount(), r.toAccountNumber()))
                .map(transferMapper::toDto)
                .findFirst()
                .orElse(null);
    }
}
