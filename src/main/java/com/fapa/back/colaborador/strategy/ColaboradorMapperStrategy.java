package com.fapa.back.colaborador.strategy;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.domain.Colaborador;

public interface ColaboradorMapperStrategy {
    Colaborador mapearParaEntidade(ColaboradorRequest request);
    void mapearPropriedades(ColaboradorRequest request, Colaborador colaborador);
    ColaboradorDetailResponse mapearParaDetailResponse(Colaborador colaborador);
}
