package com.fapa.back.colaborador.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "colaborador_producao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorProducao extends Colaborador {

    @Column(nullable = false)
    private Integer quantidadeProduzida;
    @Column(nullable = false)
    private BigDecimal valorUnidadeProduzida;

    /*
     * A produtividade deverá ser calculada pela fórmula:
     * Produtividade = Quantidade Produzida × Valor por Unidade Produzida
     * Salário Final = Salário Base + Produtividade
     * 
     */
    @Override 
    public BigDecimal calcularAdicionais() {
        return this.valorUnidadeProduzida.multiply(BigDecimal.valueOf(this.quantidadeProduzida));
    }

    @Override
    public BigDecimal calcularSalarioFinal() {
        BigDecimal produtividade = this.calcularAdicionais();
        return getSalarioBase().add(produtividade);
    }

}
