package com.juros.jurosboleto.core.domain.enums;

public enum TipoExecao {
    BOLETO_INVALIDO{
        @Override
        public String getMessage(){
            return "O boleto encontrado é inválido";
        }
    },
    TIPO_BOLETO_INVALIDO {
        @Override
        public String getMessage() {
            return "Tipo do boleto inválido, informe um boleto do tipo XPTO";
        }
    },
    BOLETO_NAO_VENCIDO {
        @Override
        public String getMessage() {
            return "O boleto informado ainda não está vencido";
        }
    };


    public abstract String getMessage();
}
