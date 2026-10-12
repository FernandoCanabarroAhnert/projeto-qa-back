package com.fapa.back.colaborador.services.validators;

import org.springframework.stereotype.Service;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.repositories.ColaboradorRepository;
import com.fapa.back.shared.exceptions.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ColaboradorServiceValidator {

    private final ColaboradorRepository colaboradorRepository;

    public void validarMatriculaExistente(String matricula) {
        if (this.matriculaExists(matricula)) {
            throw new ConflictException("Colaborador com a matrícula " + matricula + " já existe.");
        }
    }

    public void validarMatriculaExistenteParaAtualizacao(String matricula, Colaborador colaborador, ColaboradorRequest request) {
        if (isMatriculaChanged(colaborador, request) && this.matriculaExists(request.getMatricula())) {
            throw new ConflictException("Colaborador com a matrícula " + request.getMatricula() + " já existe.");
        }
    }

    private boolean isMatriculaChanged(Colaborador colaborador, ColaboradorRequest request) {
        return !colaborador.getMatricula().equals(request.getMatricula());
    }

    private boolean matriculaExists(String matricula) {
        return this.colaboradorRepository.existsByMatricula(matricula);
    }

}
