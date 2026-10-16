package com.fapa.back.colaborador.services.validators.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fapa.back.colaborador.controllers.request.ColaboradorPadraoRequest;
import com.fapa.back.shared.dtos.FieldMessage;

@DisplayName("ColaboradorPadraoRequestValidatorStrategy")
class ColaboradorPadraoRequestValidatorStrategyTest {

    private final ColaboradorPadraoRequestValidatorStrategy strategy = new ColaboradorPadraoRequestValidatorStrategy();

    @Test
    @DisplayName("não adiciona mensagens, pois PADRAO não tem validações específicas")
    void naoAdicionaMensagens() {
        List<FieldMessage> mensagens = new ArrayList<>();

        strategy.validarDados(new ColaboradorPadraoRequest(), mensagens);

        assertThat(mensagens).isEmpty();
    }

    @Test
    @DisplayName("preserva as mensagens já existentes na lista")
    void preservaMensagensExistentes() {
        List<FieldMessage> mensagens = new ArrayList<>();
        mensagens.add(new FieldMessage("nome", "Nome é obrigatório"));

        strategy.validarDados(new ColaboradorPadraoRequest(), mensagens);

        assertThat(mensagens)
            .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
            .containsExactly(tuple("nome", "Nome é obrigatório"));
    }
}