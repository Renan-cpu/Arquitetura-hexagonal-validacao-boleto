package com.juros.jurosboleto.core.port.in;

import com.juros.jurosboleto.core.domain.BoletoCalculado;

import java.time.LocalDate;

public interface CalculoBoletoPort {
    BoletoCalculado executar(String codigo, LocalDate dataPagamento);
}
