package com.juros.jurosboleto.core.port.out;

import com.juros.jurosboleto.core.domain.BoletoCalculado;

public interface SalvarCalculoBoletoPort {
    void executar(BoletoCalculado boletoCalculado);
}
