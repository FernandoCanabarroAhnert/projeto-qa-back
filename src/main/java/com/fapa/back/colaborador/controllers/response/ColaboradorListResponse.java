package com.fapa.back.colaborador.controllers.response;

import java.math.BigDecimal;

import com.fapa.back.colaborador.enums.TipoColaborador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class ColaboradorListResponse {
    private Long id;
    private String matricula;
    private String nome;
    private BigDecimal salarioBase;
    private BigDecimal adicionais;
    private BigDecimal salarioFinal;
    private TipoColaborador tipoColaborador;
}
