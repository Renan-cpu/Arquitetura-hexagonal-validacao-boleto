package com.juros.jurosboleto.adapter.in.web;

import com.juros.jurosboleto.adapter.in.web.dto.BoletoCalculadoResponse;
import com.juros.jurosboleto.adapter.in.web.dto.CalculaBoletoRequest;
import com.juros.jurosboleto.core.domain.BoletoCalculado;
import com.juros.jurosboleto.core.port.in.CalculoBoletoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boleto")
@RequiredArgsConstructor
public class BoletoControllerImpl implements BoletoController {

    private final CalculoBoletoPort calculoBoletoPort;

    @Override
    @PostMapping("/calcular-juros")
    public ResponseEntity<BoletoCalculadoResponse> calcularJuros(@RequestBody CalculaBoletoRequest request) {
        BoletoCalculado boletoCalculado = calculoBoletoPort.executar(
                request.getCodigo(),
                request.getDataPagamento().toLocalDate()
        );
        return ResponseEntity.ok(BoletoCalculadoResponse.from(boletoCalculado));
    }

}
