package com.itau.challenge.bank.application.mapper;

import com.itau.challenge.bank.application.dto.TransferResponseDTO;
import com.itau.challenge.bank.domain.entity.Transfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferResponseDTO toDto(Transfer transfer);
}
