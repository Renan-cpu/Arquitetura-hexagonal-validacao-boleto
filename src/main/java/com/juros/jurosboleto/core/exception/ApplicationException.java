package com.juros.jurosboleto.core.exception;

import com.juros.jurosboleto.core.domain.enums.TipoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {
    private TipoException tipoException;
}
