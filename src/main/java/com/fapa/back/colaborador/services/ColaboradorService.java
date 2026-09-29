package com.fapa.back.colaborador.services;

import org.springframework.data.domain.Page;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorListResponse;

public interface ColaboradorService {
    Page<ColaboradorListResponse> listar(
        int page, int size, String sort, String direction,
        String nomeMatricula, String tipoColaborador
    );
    ColaboradorDetailResponse obterPorId(Long id);
    ColaboradorDetailResponse criar(ColaboradorRequest colaborador);
    ColaboradorDetailResponse atualizar(Long id, ColaboradorRequest colaborador);
    void deletar(Long id);
}
