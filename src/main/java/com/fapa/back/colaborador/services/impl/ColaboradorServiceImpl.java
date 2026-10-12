package com.fapa.back.colaborador.services.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorListResponse;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.colaborador.mappers.ColaboradorMapper;
import com.fapa.back.colaborador.repositories.ColaboradorRepository;
import com.fapa.back.colaborador.services.BuscarColaboradorPorIdService;
import com.fapa.back.colaborador.services.ColaboradorService;
import com.fapa.back.colaborador.services.validators.ColaboradorServiceValidator;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ColaboradorServiceImpl implements ColaboradorService {
    
    private final ColaboradorRepository colaboradorRepository;
    private final BuscarColaboradorPorIdService buscarColaboradorPorIdService;
    private final ColaboradorServiceValidator colaboradorServiceValidator;

    @Override
    public Page<ColaboradorListResponse> listar(
        int page, int size, String sort, String direction,
        String nomeMatricula, String tipoColaborador
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return this.colaboradorRepository.findByFiltro(
            nomeMatricula, 
            tipoColaborador != null ? TipoColaborador.valueOf(tipoColaborador) : null, 
            pageable
        ).map(ColaboradorMapper::toListResponse);
    }

    @Override
    public ColaboradorDetailResponse obterPorId(Long id) {
        return ColaboradorMapper.toDetailResponse(this.buscarColaboradorPorIdService.buscarPorId(id));
    }

    @Override
    public ColaboradorDetailResponse criar(ColaboradorRequest request) {
        this.colaboradorServiceValidator.validarMatriculaExistente(request.getMatricula());
        Colaborador entity = ColaboradorMapper.toEntity(request);
        entity.setCriadoEm(LocalDateTime.now());
        return ColaboradorMapper.toDetailResponse(this.colaboradorRepository.save(entity));
    }

    @Override
    public ColaboradorDetailResponse atualizar(Long id, ColaboradorRequest request) {
        Colaborador existingColaborador = this.buscarColaboradorPorIdService.buscarPorId(id);
        this.colaboradorServiceValidator.validarMatriculaExistenteParaAtualizacao(existingColaborador.getMatricula(), existingColaborador, request);
        boolean isTipoColaboradorChanged = !existingColaborador.getTipoColaborador().name().equals(request.getTipoColaborador());
        if (isTipoColaboradorChanged) {
            LocalDateTime criadoEm = existingColaborador.getCriadoEm(); 
            existingColaborador = ColaboradorMapper.toEntity(request);
            existingColaborador.setCriadoEm(criadoEm);
            this.colaboradorRepository.deleteById(id);
        }
        else {
            ColaboradorMapper.atualizarEntidade(existingColaborador, request);
        }
        existingColaborador.setAtualizadoEm(LocalDateTime.now());
        return ColaboradorMapper.toDetailResponse(this.colaboradorRepository.save(existingColaborador));
    }

    @Override
    public void deletar(Long id) {
        this.buscarColaboradorPorIdService.buscarPorId(id);
        this.colaboradorRepository.deleteById(id);
    }

}
