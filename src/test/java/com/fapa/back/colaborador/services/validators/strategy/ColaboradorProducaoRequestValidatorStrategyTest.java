package com.fapa.back.colaborador.services.validators.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;
import com.fapa.back.shared.dtos.FieldMessage;

@DisplayName("ColaboradorProducaoRequestValidatorStrategy")
class ColaboradorProducaoRequestValidatorStrategyTest {

    private final ColaboradorProducaoRequestValidatorStrategy strategy = new ColaboradorProducaoRequestValidatorStrategy();

    @Test
    @DisplayName("não adiciona mensagens para valores válidos")
    void aceitaValoresValidos() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestProducao(10, new BigDecimal("5.5")), mensagens);

        assertThat(mensagens).isEmpty();
    }

    @Test
    @DisplayName("aceita quantidade e valor unitário iguais a zero")
    void aceitaZero() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestProducao(0, BigDecimal.ZERO), mensagens);

        assertThat(mensagens).isEmpty();
    }

    @Test
    @DisplayName("rejeita quantidadeProduzida nula")
    void rejeitaQuantidadeNula() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestProducao(null, BigDecimal.TEN), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("quantidadeProduzida", "Quantidade produzida é obrigatório"));
    }

    @Test
    @DisplayName("rejeita quantidadeProduzida negativa")
    void rejeitaQuantidadeNegativa() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestProducao(-1, BigDecimal.TEN), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("quantidadeProduzida", "Quantidade produzida deve ser maior ou igual a zero"));
    }

    @Test
    @DisplayName("rejeita valorUnidadeProduzida nulo")
    void rejeitaValorUnidadeNulo() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestProducao(10, null), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("valorUnidadeProduzida", "Valor da unidade produzida é obrigatório"));
    }

    @Test
    @DisplayName("rejeita valorUnidadeProduzida negativo")
    void rejeitaValorUnidadeNegativo() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestProducao(10, new BigDecimal("-0.01")), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("valorUnidadeProduzida", "Valor da unidade produzida deve ser maior ou igual a zero"));
    }

    @Test
    @DisplayName("acumula os erros de quantidade e valor unitário, nessa ordem")
    void acumulaErros() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestProducao(null, null), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(
                tuple("quantidadeProduzida", "Quantidade produzida é obrigatório"),
                tuple("valorUnidadeProduzida", "Valor da unidade produzida é obrigatório"));
    }

    private static ColaboradorProducaoRequest requestProducao(Integer quantidade, BigDecimal valorUnitario) {
        ColaboradorProducaoRequest request = new ColaboradorProducaoRequest();
        request.setQuantidadeProduzida(quantidade);
        request.setValorUnidadeProduzida(valorUnitario);
        return request;
    }
}