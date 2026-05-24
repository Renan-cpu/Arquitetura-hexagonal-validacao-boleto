package com.juros.jurosboleto.adapter.datasource.integration.client;

import com.juros.jurosboleto.adapter.datasource.integration.dto.BoletoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "complemento", url = "${api.boleto}")
public interface ComplementoBoletoClinet {

    @GetMapping("/{codigo}")
    BoletoDTO getBoleto(@PathVariable("codigo") String codigo);

}
