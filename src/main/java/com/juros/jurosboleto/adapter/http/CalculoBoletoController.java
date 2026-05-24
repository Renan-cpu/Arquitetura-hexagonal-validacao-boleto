package com.juros.jurosboleto.adapter.http;

import com.juros.jurosboleto.adapter.http.dto.CalculoBoletoRequest;
import com.juros.jurosboleto.adapter.http.dto.CalculoBoletoResponse;
import com.juros.jurosboleto.adapter.http.mapper.CalculoBoletoMapper;
import com.juros.jurosboleto.core.port.in.CalculoBoletoPort;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/boleto")
public class CalculoBoletoController {

    private final CalculoBoletoPort calculoBoletoPort;
    private final CalculoBoletoMapper map;

    @PostMapping("/calcular")
    @Operation(summary = "Calcular boleto", description = "Calcula o valor do boleto com juros, caso haja atraso no pagamento.")
    public ResponseEntity<CalculoBoletoResponse> calcularBoleto(@Valid @RequestBody CalculoBoletoRequest boleto) {
        var boletoCalculado = calculoBoletoPort.executar(boleto.getCodigo(), boleto.getDataPagamento());
        return ResponseEntity.ok(map.toDTO(boletoCalculado));
    }
}
