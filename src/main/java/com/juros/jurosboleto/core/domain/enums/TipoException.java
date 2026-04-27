package com.juros.jurosboleto.core.domain.enums;

public enum TipoException {
    BOLETO_INVALIDO{
        @Override
        public String getMessageErro() {
            return "Boleto inválido.";
        }
    },
    TIPO_BOLETO_INVALIDO{
        @Override
        public String getMessageErro() {
            return "Tipo de boleto inválido.";
        }
    },
    BOLETO_NAO_VENCIDO{
        @Override
        public String getMessageErro() {
            return "Boleto não vencido.";
        }
    };

    public abstract String getMessageErro();
}
