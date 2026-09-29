package com.fapa.back.colaborador.controllers.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class ColaboradorComissionadoDetailResponse extends ColaboradorDetailResponse {
    private BigDecimal valorVendas;
    private BigDecimal porcentagemComissao;
}
