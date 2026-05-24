package com.juros.jurosboleto.adapter.datasource.mapper;

import com.juros.jurosboleto.adapter.datasource.database.entity.BoletoCalculadoEntity;
import com.juros.jurosboleto.core.domain.BoletoCalculado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoletoCalculadoMapper {

    BoletoCalculadoEntity toEntity(BoletoCalculado boletoCalculado);
}
