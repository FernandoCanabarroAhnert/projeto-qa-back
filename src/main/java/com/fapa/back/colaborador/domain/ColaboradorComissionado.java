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
@Table(name = "colaborador_comissionado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorComissionado extends Colaborador {

    @Column(nullable = false)
    private BigDecimal valorVendas;
    @Column(nullable = false)
    private BigDecimal porcentagemComissao;

    /*
     * Comissão = Valor das Vendas × Percentual de Comissão
     * Salário Final = Salário Base + Comissão
     */
    @Override 
    public BigDecimal calcularAdicionais() {
        return this.valorVendas.multiply(this.porcentagemComissao.divide(BigDecimal.valueOf(100)));
    }

    @Override
    public BigDecimal calcularSalarioFinal() {
        BigDecimal comissao = calcularAdicionais();
        return getSalarioBase().add(comissao);
    }

}
