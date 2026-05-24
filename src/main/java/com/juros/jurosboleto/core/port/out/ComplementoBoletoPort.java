package com.juros.jurosboleto.core.port.out;

import com.juros.jurosboleto.core.domain.Boleto;

public interface ComplementoBoletoPort {
    Boleto executar(String codigo);
}
