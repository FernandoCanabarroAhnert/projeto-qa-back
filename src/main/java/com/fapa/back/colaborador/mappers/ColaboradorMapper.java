package com.fapa.back.colaborador.mappers;

import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorListResponse;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.colaborador.strategy.ColaboradorComissionadoMapperStrategy;
import com.fapa.back.colaborador.strategy.ColaboradorMapperStrategy;
import com.fapa.back.colaborador.strategy.ColaboradorPadraoMapperStrategy;
import com.fapa.back.colaborador.strategy.ColaboradorProducaoMapperStrategy;

@Component 
public class ColaboradorMapper {

    private static final Map<TipoColaborador, ColaboradorMapperStrategy> strategyMap = Map.of(
        TipoColaborador.PADRAO, new ColaboradorPadraoMapperStrategy(),
        TipoColaborador.PRODUCAO, new ColaboradorProducaoMapperStrategy(),
        TipoColaborador.COMISSIONADO, new ColaboradorComissionadoMapperStrategy()
    );

    public static ColaboradorListResponse toListResponse(Colaborador colaborador) {
        if (colaborador == null) {
            return null;
        }
        ColaboradorListResponse response = ColaboradorListResponse.builder()
            .id(colaborador.getId())
            .matricula(colaborador.getMatricula())
            .nome(colaborador.getNome())
            .salarioBase(colaborador.getSalarioBase().setScale(2, RoundingMode.HALF_UP))
            .adicionais(colaborador.calcularAdicionais().setScale(2, RoundingMode.HALF_UP))
            .salarioFinal(colaborador.calcularSalarioFinal().setScale(2, RoundingMode.HALF_UP))
            .tipoColaborador(colaborador.getTipoColaborador())
            .build();
        return response;
    }

    public static ColaboradorDetailResponse toDetailResponse(Colaborador colaborador) {
        if (colaborador == null) {
            return null;
        }
        return strategyMap.get(colaborador.getTipoColaborador()).mapearParaDetailResponse(colaborador);
    }

    public static Colaborador toEntity(ColaboradorRequest request) {
        if (request == null) {
            return null;
        }
        return strategyMap.get(TipoColaborador.valueOf(request.getTipoColaborador())).mapearParaEntidade(request);
    }

    public static void atualizarEntidade(Colaborador existingColaborador, ColaboradorRequest request) {
        if (existingColaborador == null || request == null) {
            return;
        }
        strategyMap.get(TipoColaborador.valueOf(request.getTipoColaborador())).mapearPropriedades(request, existingColaborador);
    }

}
