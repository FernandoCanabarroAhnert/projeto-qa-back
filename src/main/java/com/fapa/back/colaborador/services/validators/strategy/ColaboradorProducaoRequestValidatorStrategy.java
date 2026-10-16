package com.fapa.back.colaborador.services.validators.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.shared.dtos.FieldMessage;

public class ColaboradorProducaoRequestValidatorStrategy implements ColaboradorRequestValidatorStrategy {

    @Override
    public void validarDados(ColaboradorRequest request, List<FieldMessage> fieldMessages) {
        ColaboradorProducaoRequest colaboradorProducaoRequest = (ColaboradorProducaoRequest) request;
        if (colaboradorProducaoRequest.getQuantidadeProduzida() == null) {
            fieldMessages.add(new FieldMessage("quantidadeProduzida", "Quantidade produzida é obrigatório"));
        }
        if (colaboradorProducaoRequest.getQuantidadeProduzida() != null && colaboradorProducaoRequest.getQuantidadeProduzida() < 0) {
            fieldMessages.add(new FieldMessage("quantidadeProduzida", "Quantidade produzida deve ser maior ou igual a zero"));
        }
        if (colaboradorProducaoRequest.getValorUnidadeProduzida() == null) {
            fieldMessages.add(new FieldMessage("valorUnidadeProduzida", "Valor da unidade produzida é obrigatório"));
        }
        if (colaboradorProducaoRequest.getValorUnidadeProduzida() != null && colaboradorProducaoRequest.getValorUnidadeProduzida().compareTo(BigDecimal.ZERO) < 0) {
            fieldMessages.add(new FieldMessage("valorUnidadeProduzida", "Valor da unidade produzida deve ser maior ou igual a zero"));
        }
    }

}
