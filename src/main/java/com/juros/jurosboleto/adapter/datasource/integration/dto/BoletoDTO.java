package com.juros.jurosboleto.adapter.datasource.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juros.jurosboleto.core.domain.enums.TipoBoleto;
import feign.form.FormProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BoletoDTO {
    private String codigo;

    @JsonProperty("data_vencimento")
    private LocalDate dataVencimento;

    private BigDecimal valor;

    private TipoBoleto tipo;

}
