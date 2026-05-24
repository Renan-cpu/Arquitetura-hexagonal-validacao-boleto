package com.juros.jurosboleto.adapter.http.mapper;

import com.juros.jurosboleto.adapter.http.dto.CalculoBoletoResponse;
import com.juros.jurosboleto.core.domain.BoletoCalculado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CalculoBoletoMapper {

    CalculoBoletoResponse toDTO(BoletoCalculado boletoCalculado);
}
