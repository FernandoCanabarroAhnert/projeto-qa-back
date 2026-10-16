package com.fapa.back.colaborador.services.validators.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.shared.dtos.FieldMessage;

@DisplayName("ColaboradorComissionadoRequestValidatorStrategy")
class ColaboradorComissionadoRequestValidatorStrategyTest {

    private final ColaboradorComissionadoRequestValidatorStrategy strategy =
        new ColaboradorComissionadoRequestValidatorStrategy();

    @Test
    @DisplayName("não adiciona mensagens para valores válidos")
    void aceitaValoresValidos() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestComissionado(new BigDecimal("1000"), new BigDecimal("10")), mensagens);

        assertThat(mensagens).isEmpty();
    }

    @Test
    @DisplayName("aceita valores zero para vendas e porcentagem")
    void aceitaZero() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestComissionado(BigDecimal.ZERO, BigDecimal.ZERO), mensagens);

        assertThat(mensagens).isEmpty();
    }

    @Test
    @DisplayName("rejeita valorVendas nulo")
    void rejeitaValorVendasNulo() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestComissionado(null, BigDecimal.TEN), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("valorVendas", "Valor das vendas é obrigatório"));
    }

    @Test
    @DisplayName("rejeita valorVendas negativo")
    void rejeitaValorVendasNegativo() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestComissionado(new BigDecimal("-0.01"), BigDecimal.TEN), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("valorVendas", "Valor das vendas deve ser maior ou igual a zero"));
    }

    @Test
    @DisplayName("rejeita porcentagemComissao nula")
    void rejeitaPorcentagemNula() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestComissionado(BigDecimal.TEN, null), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("porcentagemComissao", "Porcentagem de comissão é obrigatória"));
    }

    @Test
    @DisplayName("rejeita porcentagemComissao negativa")
    void rejeitaPorcentagemNegativa() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestComissionado(BigDecimal.TEN, new BigDecimal("-5")), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("porcentagemComissao", "Porcentagem de comissão deve ser maior ou igual a zero"));
    }

    @Test
    @DisplayName("acumula os erros de valorVendas e porcentagemComissao, nessa ordem")
    void acumulaErros() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(requestComissionado(null, null), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(
                tuple("valorVendas", "Valor das vendas é obrigatório"),
                tuple("porcentagemComissao", "Porcentagem de comissão é obrigatória"));
    }

    private static ColaboradorComissionadoRequest requestComissionado(BigDecimal valorVendas, BigDecimal porcentagem) {
        ColaboradorComissionadoRequest request = new ColaboradorComissionadoRequest();
        request.setValorVendas(valorVendas);
        request.setPorcentagemComissao(porcentagem);
        return request;
    }
}