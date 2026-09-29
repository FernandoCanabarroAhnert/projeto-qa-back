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
public class ColaboradorComissionadoRequest extends ColaboradorRequest {
    @NotNull(message = "Valor das vendas é obrigatório")
    @PositiveOrZero(message = "Valor das vendas deve ser maior ou igual a zero")
    private BigDecimal valorVendas;
    @NotNull(message = "Porcentagem de comissão é obrigatória")
    @PositiveOrZero(message = "Porcentagem de comissão deve ser maior ou igual a zero")
    private BigDecimal porcentagemComissao;
}
