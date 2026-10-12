package com.fapa.back.colaborador.controllers.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColaboradorProducaoRequest extends ColaboradorRequest {
    @NotNull(message = "Quantidade produzida é obrigatório")
    @PositiveOrZero(message = "Quantidade produzida deve ser maior ou igual a zero")
    private Integer quantidadeProduzida;
    @NotNull(message = "Valor da unidade produzida é obrigatório")
    @PositiveOrZero(message = "Valor da unidade produzida deve ser maior ou igual a zero")
    private BigDecimal valorUnidadeProduzida;
}
