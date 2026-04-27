package com.juros.jurosboleto.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalculaBoletoRequest {

    private String codigo;

    @JsonProperty("data_pagamento")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataPagamento;

}
