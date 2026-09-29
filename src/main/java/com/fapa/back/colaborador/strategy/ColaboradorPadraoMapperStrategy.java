package com.fapa.back.colaborador.strategy;

import java.math.RoundingMode;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorPadraoDetailResponse;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.domain.ColaboradorPadrao;
import com.fapa.back.colaborador.enums.TipoColaborador;

public class ColaboradorPadraoMapperStrategy implements ColaboradorMapperStrategy {

    @Override
    public Colaborador mapearParaEntidade(ColaboradorRequest request) {
        if (request == null) {
            return null;
        }
        ColaboradorPadrao colaborador = new ColaboradorPadrao();
        mapearPropriedades(request, colaborador);
        return colaborador;
    }

    @Override
    public void mapearPropriedades(ColaboradorRequest request, Colaborador colaborador) {
        colaborador.setMatricula(request.getMatricula());
        colaborador.setNome(request.getNome());
        colaborador.setSalarioBase(request.getSalarioBase());
        colaborador.setTipoColaborador(TipoColaborador.valueOf(request.getTipoColaborador()));
    }

    @Override 
    public ColaboradorDetailResponse mapearParaDetailResponse(Colaborador colaborador) {
        if (colaborador == null) {
            return null;
        }
        ColaboradorPadrao padrao = (ColaboradorPadrao) colaborador;
        ColaboradorPadraoDetailResponse response = new ColaboradorPadraoDetailResponse();
        response.setId(padrao.getId());
        response.setMatricula(padrao.getMatricula());
        response.setNome(padrao.getNome());
        response.setSalarioBase(padrao.getSalarioBase().setScale(2, RoundingMode.HALF_UP));
        response.setAdicionais(padrao.calcularAdicionais().setScale(2, RoundingMode.HALF_UP));
        response.setSalarioFinal(padrao.calcularSalarioFinal().setScale(2, RoundingMode.HALF_UP));
        response.setTipoColaborador(padrao.getTipoColaborador());
        return response;
    }

}
