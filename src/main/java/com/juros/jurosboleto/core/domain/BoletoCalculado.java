package com.juros.jurosboleto.core.domain;

import com.juros.jurosboleto.core.domain.enums.TipoBoleto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class BoletoCalculado {

    private String codigo;
    private BigDecimal valorOriginal;
    private BigDecimal valorFinal;
    private LocalDate dataVencimento;
    private LocalDate    dataPagamento;
    private BigDecimal juros;
    private TipoBoleto tipoBoleto;

}
