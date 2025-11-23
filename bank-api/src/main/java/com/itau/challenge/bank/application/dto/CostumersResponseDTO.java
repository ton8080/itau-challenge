package com.itau.challenge.bank.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CostumersResponseDTO(String name, @JsonAlias("active_account") Boolean activeAccount) {
}
