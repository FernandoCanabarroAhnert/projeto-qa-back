package com.fapa.back.colaborador.services.validators.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.shared.dtos.FieldMessage;

public class ColaboradorComissionadoRequestValidatorStrategy implements ColaboradorRequestValidatorStrategy {

    @Override
    public void validarDados(ColaboradorRequest request, List<FieldMessage> fieldMessages) {
        ColaboradorComissionadoRequest colaboradorComissionadoRequest = (ColaboradorComissionadoRequest) request;
        if (colaboradorComissionadoRequest.getValorVendas() == null) {
            fieldMessages.add(new FieldMessage("valorVendas", "Valor das vendas é obrigatório"));
        }
        if (colaboradorComissionadoRequest.getValorVendas() != null && colaboradorComissionadoRequest.getValorVendas().compareTo(BigDecimal.ZERO) < 0) {
            fieldMessages.add(new FieldMessage("valorVendas", "Valor das vendas deve ser maior ou igual a zero"));
        }
        if (colaboradorComissionadoRequest.getPorcentagemComissao() == null) {
            fieldMessages.add(new FieldMessage("porcentagemComissao", "Porcentagem de comissão é obrigatória"));
        }
        if (colaboradorComissionadoRequest.getPorcentagemComissao() != null && colaboradorComissionadoRequest.getPorcentagemComissao().compareTo(BigDecimal.ZERO) < 0) {
            fieldMessages.add(new FieldMessage("porcentagemComissao", "Porcentagem de comissão deve ser maior ou igual a zero"));
        }
    }

}
