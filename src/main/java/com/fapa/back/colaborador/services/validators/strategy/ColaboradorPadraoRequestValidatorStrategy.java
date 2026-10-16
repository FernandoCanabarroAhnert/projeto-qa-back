package com.fapa.back.colaborador.services.validators.strategy;

import java.util.List;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.shared.dtos.FieldMessage;

public class ColaboradorPadraoRequestValidatorStrategy implements ColaboradorRequestValidatorStrategy {

    @Override
    public void validarDados(ColaboradorRequest request, List<FieldMessage> fieldMessages) {
        // Nenhuma validação específica para ColaboradorPadrao
    }

}
