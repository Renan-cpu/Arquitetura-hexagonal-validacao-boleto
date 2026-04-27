package com.juros.jurosboleto.core.usecase;

import com.juros.jurosboleto.core.domain.Boleto;
import com.juros.jurosboleto.core.domain.BoletoCalculado;
import com.juros.jurosboleto.core.domain.enums.TipoBoleto;
import com.juros.jurosboleto.core.domain.enums.TipoException;
import com.juros.jurosboleto.core.exception.ApplicationException;
import com.juros.jurosboleto.core.port.in.CalculoBoletoPort;
import com.juros.jurosboleto.core.port.out.ComplementoBoletoPort;
import com.juros.jurosboleto.core.port.out.SalvarCalculoBoletoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CalculoBoletoUseCase implements CalculoBoletoPort {

    private static final BigDecimal JUROS_DIARIO = BigDecimal.valueOf(0.033);

    private final ComplementoBoletoPort complementoBoletoPort;
    private final SalvarCalculoBoletoPort salvarCalculoBoletoPort;

    @Override
    public BoletoCalculado executar(String codigo, LocalDate dataPagamento) {
        Boleto boleto = complementoBoletoPort.executar(codigo);
        validarBoleto(boleto);

        var diasVencido = getDiasVencimento(boleto.getDataVencimento(), dataPagamento);
        var jurosBoleto = JUROS_DIARIO.multiply(boleto.getValor()).multiply(BigDecimal.valueOf(diasVencido)).setScale(2, RoundingMode.HALF_EVEN);

        BoletoCalculado boletoCalculado = BoletoCalculado.builder()
                .codigo(boleto.getCodigo())
                .dataPagamento(dataPagamento)
                .dataVencimento(boleto.getDataVencimento())
                .valorOriginal(boleto.getValor())
                .valorFinal(boleto.getValor().add(jurosBoleto))
                .juros(jurosBoleto)
                .tipoBoleto(boleto.getTipo())
                .build();

        salvarCalculoBoletoPort.executar(boletoCalculado);

        return boletoCalculado;
    }

    private void validarBoleto(Boleto boleto) {
        if (boleto == null) {
            throw new ApplicationException(TipoException.BOLETO_INVALIDO);
        }

        if (boleto.getTipo() != TipoBoleto.XPTO) {
            throw new ApplicationException(TipoException.TIPO_BOLETO_INVALIDO);
        }

        if (boleto.getDataVencimento().isAfter(LocalDate.now())) {
            throw new ApplicationException(TipoException.BOLETO_NAO_VENCIDO);
        }
    }

    private Long getDiasVencimento(LocalDate dataVencimento, LocalDate dataPagamento) {
        return ChronoUnit.DAYS.between(dataVencimento, dataPagamento);
    }

}
