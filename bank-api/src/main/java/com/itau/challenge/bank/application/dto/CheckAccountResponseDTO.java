package com.itau.challenge.bank.application.dto;

import java.math.BigDecimal;

public record CheckAccountResponseDTO(String accountNumber,
                                      boolean activeAccount,
                                      BigDecimal balance,
                                      BigDecimal dailyTransferred,
                                      String customerName) {
}
