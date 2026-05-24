package com.juros.jurosboleto.adapter.http.exception;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private List<String> mensagens = new ArrayList<>();
    private String erro;
    private int codigo;
    private Date timestamp;
    private String path;
}
