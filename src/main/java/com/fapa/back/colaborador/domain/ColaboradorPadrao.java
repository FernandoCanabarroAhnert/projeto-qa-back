package com.fapa.back.colaborador.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "colaborador_padrao")
@Getter 
@Setter 
@NoArgsConstructor 
public class ColaboradorPadrao extends Colaborador {

    @Override 
    public BigDecimal calcularAdicionais() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calcularSalarioFinal() {
        return getSalarioBase();
    }

}
