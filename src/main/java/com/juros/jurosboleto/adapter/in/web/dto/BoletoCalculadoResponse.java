package com.juros.jurosboleto.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juros.jurosboleto.core.domain.BoletoCalculado;
import com.juros.jurosboleto.core.domain.enums.TipoBoleto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BoletoCalculadoResponse {

    private String codigo;

    @JsonProperty("data_vencimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;

    private BigDecimal valor;

    private TipoBoleto tipo;

    public static BoletoCalculadoResponse from(BoletoCalculado boletoCalculado) {
        BoletoCalculadoResponse response = new BoletoCalculadoResponse();
        response.setCodigo(boletoCalculado.getCodigo());
        response.setDataVencimento(boletoCalculado.getDataVencimento());
        response.setValor(boletoCalculado.getValorFinal());
        response.setTipo(boletoCalculado.getTipoBoleto());
        return response;
    }

}
