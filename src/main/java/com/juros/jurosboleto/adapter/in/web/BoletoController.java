package com.juros.jurosboleto.adapter.in.web;

import com.juros.jurosboleto.adapter.in.web.dto.BoletoCalculadoResponse;
import com.juros.jurosboleto.adapter.in.web.dto.CalculaBoletoRequest;
import org.springframework.http.ResponseEntity;

public interface BoletoController {

    ResponseEntity<BoletoCalculadoResponse> calcularJuros(CalculaBoletoRequest request);

}
