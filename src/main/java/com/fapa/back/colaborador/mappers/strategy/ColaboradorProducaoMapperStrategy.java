package com.fapa.back.colaborador.mappers.strategy;

import java.math.RoundingMode;

import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorProducaoDetailResponse;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.domain.ColaboradorProducao;
import com.fapa.back.colaborador.enums.TipoColaborador;

public class ColaboradorProducaoMapperStrategy implements ColaboradorMapperStrategy {

    @Override
    public Colaborador mapearParaEntidade(ColaboradorRequest request) {
        if (request == null) {
            return null;
        }
        ColaboradorProducao colaborador = new ColaboradorProducao();
        mapearPropriedades(request, colaborador);
        return colaborador;
    }

    @Override
    public void mapearPropriedades(ColaboradorRequest request, Colaborador colaborador) {
        ColaboradorProducaoRequest producaoRequest = (ColaboradorProducaoRequest) request;
        ColaboradorProducao producao = (ColaboradorProducao) colaborador;
        producao.setMatricula(request.getMatricula());
        producao.setNome(request.getNome());
        producao.setSalarioBase(request.getSalarioBase());
        producao.setTipoColaborador(TipoColaborador.PRODUCAO);
        producao.setQuantidadeProduzida(producaoRequest.getQuantidadeProduzida());
        producao.setValorUnidadeProduzida(producaoRequest.getValorUnidadeProduzida());
    }

    @Override 
    public ColaboradorDetailResponse mapearParaDetailResponse(Colaborador colaborador) {
        if (colaborador == null) {
            return null;
        }
        ColaboradorProducao producao = (ColaboradorProducao) colaborador;
        ColaboradorProducaoDetailResponse response = new ColaboradorProducaoDetailResponse();
        response.setId(producao.getId());
        response.setMatricula(producao.getMatricula());
        response.setNome(producao.getNome());
        response.setSalarioBase(producao.getSalarioBase().setScale(2, RoundingMode.HALF_UP));
        response.setAdicionais(producao.calcularAdicionais().setScale(2, RoundingMode.HALF_UP));
        response.setSalarioFinal(producao.calcularSalarioFinal().setScale(2, RoundingMode.HALF_UP));
        response.setTipoColaborador(producao.getTipoColaborador());
        response.setQuantidadeProduzida(producao.getQuantidadeProduzida());
        response.setValorUnidadeProduzida(producao.getValorUnidadeProduzida().setScale(2, RoundingMode.HALF_UP));
        return response;
    }



}
