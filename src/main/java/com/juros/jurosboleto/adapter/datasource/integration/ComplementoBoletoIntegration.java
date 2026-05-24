package com.juros.jurosboleto.adapter.datasource.integration;

import com.juros.jurosboleto.adapter.datasource.integration.client.ComplementoBoletoClinet;
import com.juros.jurosboleto.adapter.datasource.mapper.BoletoMapper;
import com.juros.jurosboleto.core.domain.Boleto;
import com.juros.jurosboleto.core.port.out.ComplementoBoletoPort;
import org.springframework.stereotype.Component;

@Component
public class ComplementoBoletoIntegration implements ComplementoBoletoPort {

    private final ComplementoBoletoClinet complementoBoletoClinet;
    private final BoletoMapper boletoMapper;

    public ComplementoBoletoIntegration(ComplementoBoletoClinet complementoBoletoClinet, BoletoMapper boletoMapper) {
        this.complementoBoletoClinet = complementoBoletoClinet;
        this.boletoMapper = boletoMapper;
    }

    @Override
    public Boleto executar(String codigo) {
        var boletoDTO = complementoBoletoClinet.getBoleto(codigo);
        return boletoMapper.toDomain(boletoDTO);
    }
}
