package com.fapa.back.colaborador.strategy;

import java.math.RoundingMode;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorComissionadoDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.domain.ColaboradorComissionado;
import com.fapa.back.colaborador.enums.TipoColaborador;

public class ColaboradorComissionadoMapperStrategy implements ColaboradorMapperStrategy {

    @Override
    public Colaborador mapearParaEntidade(ColaboradorRequest request) {
        if (request == null) {
            return null;
        }
        ColaboradorComissionado colaborador = new ColaboradorComissionado();
        mapearPropriedades(request, colaborador);
        return colaborador;
    }

    @Override
    public void mapearPropriedades(ColaboradorRequest request, Colaborador colaborador) {
        ColaboradorComissionadoRequest comissionadoRequest = (ColaboradorComissionadoRequest) request;
        ColaboradorComissionado comissionado = (ColaboradorComissionado) colaborador;
        comissionado.setMatricula(request.getMatricula());
        comissionado.setNome(request.getNome());
        comissionado.setSalarioBase(request.getSalarioBase());
        comissionado.setTipoColaborador(TipoColaborador.valueOf(request.getTipoColaborador()));
        comissionado.setValorVendas(comissionadoRequest.getValorVendas());
        comissionado.setPorcentagemComissao(comissionadoRequest.getPorcentagemComissao());
    }

    @Override 
    public ColaboradorDetailResponse mapearParaDetailResponse(Colaborador colaborador) {
        if (colaborador == null) {
            return null;
        }
        ColaboradorComissionado comissionado = (ColaboradorComissionado) colaborador;
        ColaboradorComissionadoDetailResponse response = new ColaboradorComissionadoDetailResponse();
        response.setId(comissionado.getId());
        response.setMatricula(comissionado.getMatricula());
        response.setNome(comissionado.getNome());
        response.setSalarioBase(comissionado.getSalarioBase().setScale(2, RoundingMode.HALF_UP));
        response.setAdicionais(comissionado.calcularAdicionais().setScale(2, RoundingMode.HALF_UP));
        response.setSalarioFinal(comissionado.calcularSalarioFinal().setScale(2, RoundingMode.HALF_UP));
        response.setTipoColaborador(comissionado.getTipoColaborador());
        response.setValorVendas(comissionado.getValorVendas().setScale(2, RoundingMode.HALF_UP));
        response.setPorcentagemComissao(comissionado.getPorcentagemComissao());
        return response;
    }



}
