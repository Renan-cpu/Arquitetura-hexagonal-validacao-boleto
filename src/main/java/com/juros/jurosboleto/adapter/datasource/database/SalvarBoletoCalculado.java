package com.juros.jurosboleto.adapter.datasource.database;

import com.juros.jurosboleto.adapter.datasource.database.repository.BoletoCalculadoRepository;
import com.juros.jurosboleto.adapter.datasource.mapper.BoletoCalculadoMapper;
import com.juros.jurosboleto.core.domain.BoletoCalculado;
import com.juros.jurosboleto.core.port.out.SalvarCalculoBoletoPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SalvarBoletoCalculado implements SalvarCalculoBoletoPort {

    private final BoletoCalculadoRepository boletoCalculadoRepository;
    private final BoletoCalculadoMapper boletoCalculadoMapper;

    @Override
    public void executar(BoletoCalculado boletoCalculado) {
        var boletoConvertido = boletoCalculadoMapper.toEntity(boletoCalculado);
        boletoCalculadoRepository.save(boletoConvertido);
    }
}
