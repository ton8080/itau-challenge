package com.itau.challenge.bank.application.controller;

import com.itau.challenge.bank.application.dto.TransferRequestDTO;
import com.itau.challenge.bank.application.dto.TransferResponseDTO;
import com.itau.challenge.bank.application.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferResponseDTO> transfer(@RequestBody TransferRequestDTO request) {
        TransferResponseDTO resp = service.transfer(request);
        return ResponseEntity.ok(resp);
    }
}
