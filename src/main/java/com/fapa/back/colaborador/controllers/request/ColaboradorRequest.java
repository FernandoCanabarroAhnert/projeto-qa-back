package com.fapa.back.colaborador.controllers.request;

import java.math.BigDecimal;

import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.shared.annotations.IsEnum;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipoColaborador",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(
        value = ColaboradorComissionadoRequest.class,
        name = "COMISSIONADO"
    ),
    @JsonSubTypes.Type(
        value = ColaboradorProducaoRequest.class,
        name = "PRODUCAO"
    ),
    @JsonSubTypes.Type(
        value = ColaboradorPadraoRequest.class,
        name = "PADRAO"
    )
})
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class ColaboradorRequest {
    @NotBlank(message = "Matrícula é obrigatória")
    private String matricula;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotNull(message = "Salário base é obrigatório")
    @PositiveOrZero(message = "Salário base deve ser maior ou igual a zero")
    private BigDecimal salarioBase;
    @NotBlank(message = "Tipo de colaborador é obrigatório")
    @IsEnum(enumClass = TipoColaborador.class, message = "Tipo de colaborador inválido. Valores permitidos: PADRAO, COMISSIONADO, PRODUCAO")
    private String tipoColaborador;
}
