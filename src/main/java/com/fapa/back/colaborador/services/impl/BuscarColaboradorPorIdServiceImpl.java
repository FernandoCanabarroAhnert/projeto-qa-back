package com.fapa.back.colaborador.services.impl;

import org.springframework.stereotype.Service;

import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.repositories.ColaboradorRepository;
import com.fapa.back.colaborador.services.BuscarColaboradorPorIdService;
import com.fapa.back.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class BuscarColaboradorPorIdServiceImpl implements BuscarColaboradorPorIdService {

    private final ColaboradorRepository colaboradorRepository;

    @Override
    public Colaborador buscarPorId(Long id) {
        return this.colaboradorRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Colaborador não encontrado com o ID: " + id));
    }

}
