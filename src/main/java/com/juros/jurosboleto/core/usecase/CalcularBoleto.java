package com.juros.jurosboleto.core.usecase;

import com.juros.jurosboleto.core.domain.Boleto;
import com.juros.jurosboleto.core.domain.BoletoCalculado;
import com.juros.jurosboleto.core.domain.enums.TipoExecao;
import com.juros.jurosboleto.core.exception.ApplicationExeption;
import com.juros.jurosboleto.core.port.in.CalculoBoletoPort;
import com.juros.jurosboleto.core.port.out.ComplementoBoletoPort;
import com.juros.jurosboleto.core.port.out.SalvarCalculoBoletoPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CalcularBoleto implements CalculoBoletoPort {

    private final ComplementoBoletoPort complementoBoletoPort;
    private final SalvarCalculoBoletoPort salvarCalculoBoletoPort;

    private static final BigDecimal JUROS_DIARIO = BigDecimal.valueOf(0.033);

    public CalcularBoleto(ComplementoBoletoPort complementoBoletoPort, SalvarCalculoBoletoPort salvarCalculoBoletoPort) {
        this.complementoBoletoPort = complementoBoletoPort;
        this.salvarCalculoBoletoPort = salvarCalculoBoletoPort;
    }

    @Override
    public BoletoCalculado executar(String codigo, LocalDate dataPagamento) {
        var boleto = complementoBoletoPort.executar(codigo);

        //Validar boleto
        validar(boleto);

        //calcular boleto
        var diasVencidos = getDiasVencidos(boleto.getDataVencimento(), dataPagamento);
        var valorJurosDia = JUROS_DIARIO.multiply(boleto.getValor().divide(BigDecimal.valueOf(100)));
        var valorJuros = valorJurosDia.multiply(BigDecimal.valueOf(diasVencidos)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        var boletoCalculado = BoletoCalculado.builder()
                .codigo(boleto.getCodigo())
                .dataVencimento(boleto.getDataVencimento())
                .dataPagamento(dataPagamento)
                .valorOriginal(boleto.getValor())
                .juros(valorJuros)
                .valorComJuros(boleto.getValor().add(valorJuros))
                .tipoBoleto(boleto.getTipo())
                .build();

        //Salvar boleto
        salvarCalculoBoletoPort.executar(boletoCalculado);


        return null;
    }


    private void validar(Boleto boleto){
        if(boleto == null){
            throw new ApplicationExeption(TipoExecao.BOLETO_INVALIDO);
        }

        if(boleto.getTipo() == null){
            throw new ApplicationExeption(TipoExecao.TIPO_BOLETO_INVALIDO);
        }

        if(boleto.getDataVencimento().isAfter(LocalDate.now())){
            throw new ApplicationExeption(TipoExecao.BOLETO_NAO_VENCIDO);
        }
    }

    private Long getDiasVencidos(LocalDate dataVencimento, LocalDate dataPagamento){
        return ChronoUnit.DAYS.between(dataVencimento, dataPagamento);
    }
}
