package com.juros.jurosboleto.adapter.datasource.mapper;

import com.juros.jurosboleto.adapter.datasource.integration.dto.BoletoDTO;
import com.juros.jurosboleto.core.domain.Boleto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoletoMapper {
    Boleto toDomain(BoletoDTO boletoDTO);
}
