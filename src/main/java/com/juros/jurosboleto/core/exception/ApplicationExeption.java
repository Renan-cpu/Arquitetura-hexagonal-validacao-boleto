package com.juros.jurosboleto.core.exception;

import com.juros.jurosboleto.core.domain.enums.TipoExecao;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationExeption extends RuntimeException{
    private TipoExecao tipoExecao;

}
