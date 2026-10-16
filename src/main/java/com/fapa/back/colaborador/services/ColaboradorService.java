package com.fapa.back.colaborador.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorListResponse;
import com.fapa.back.colaborador.controllers.response.ResumoFolhaPagamentoResponse;

public interface ColaboradorService {
    Page<ColaboradorListResponse> listar(
        int page, int size, String sort, String direction,
        String nomeMatricula, String tipoColaborador
    );
    ColaboradorDetailResponse obterPorId(Long id);
    ColaboradorDetailResponse criar(ColaboradorRequest request);
    ColaboradorDetailResponse atualizar(Long id, ColaboradorRequest request);
    void deletar(Long id);
    List<ResumoFolhaPagamentoResponse> gerarResumoFolhaPagamento();
}
