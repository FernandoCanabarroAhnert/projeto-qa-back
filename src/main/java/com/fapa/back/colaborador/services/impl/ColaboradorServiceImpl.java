package com.fapa.back.colaborador.services.impl;

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
import com.fapa.back.shared.exceptions.BadRequestException;
import com.fapa.back.shared.exceptions.ConflictException;
import com.fapa.back.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ColaboradorServiceImpl implements ColaboradorService {
    
    private final ColaboradorRepository colaboradorRepository;
    private final BuscarColaboradorPorIdService buscarColaboradorPorIdService;

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
    public ColaboradorDetailResponse criar(ColaboradorRequest colaborador) {
        if (this.colaboradorRepository.existsByMatricula(colaborador.getMatricula())) {
            throw new ConflictException("Colaborador com a matrícula " + colaborador.getMatricula() + " já existe.");
        }
        Colaborador entity = ColaboradorMapper.toEntity(colaborador);
        return ColaboradorMapper.toDetailResponse(this.colaboradorRepository.save(entity));
    }

    @Override
    public ColaboradorDetailResponse atualizar(Long id, ColaboradorRequest colaborador) {
        Colaborador existingColaborador = this.buscarColaboradorPorIdService.buscarPorId(id);
        boolean isMatriculaChanged = !existingColaborador.getMatricula().equals(colaborador.getMatricula());
        if (isMatriculaChanged && this.colaboradorRepository.existsByMatricula(colaborador.getMatricula())) {
            throw new ConflictException("Colaborador com a matrícula " + colaborador.getMatricula() + " já existe.");
        }
        boolean isTipoColaboradorChanged = !existingColaborador.getTipoColaborador().name().equals(colaborador.getTipoColaborador());
        if (isTipoColaboradorChanged) {
            throw new BadRequestException("Não é permitido alterar o tipo de colaborador. Por favor, crie um novo colaborador com o tipo desejado.");
        }
        ColaboradorMapper.atualizarEntidade(existingColaborador, colaborador);
        return ColaboradorMapper.toDetailResponse(this.colaboradorRepository.save(existingColaborador));
    }

    @Override
    public void deletar(Long id) {
        if (!this.colaboradorRepository.existsById(id)) {
            throw new NotFoundException("Colaborador não encontrado com o ID: " + id);
        }
        this.colaboradorRepository.deleteById(id);
    }

}
