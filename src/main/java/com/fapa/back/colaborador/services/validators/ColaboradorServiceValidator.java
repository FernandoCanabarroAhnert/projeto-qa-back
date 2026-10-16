package com.fapa.back.colaborador.services.validators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.colaborador.repositories.ColaboradorRepository;
import com.fapa.back.colaborador.services.validators.strategy.ColaboradorComissionadoRequestValidatorStrategy;
import com.fapa.back.colaborador.services.validators.strategy.ColaboradorPadraoRequestValidatorStrategy;
import com.fapa.back.colaborador.services.validators.strategy.ColaboradorProducaoRequestValidatorStrategy;
import com.fapa.back.colaborador.services.validators.strategy.ColaboradorRequestValidatorStrategy;
import com.fapa.back.shared.dtos.FieldMessage;
import com.fapa.back.shared.exceptions.BadRequestException;
import com.fapa.back.shared.exceptions.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ColaboradorServiceValidator {

    private final ColaboradorRepository colaboradorRepository;

    private static final Map<String, ColaboradorRequestValidatorStrategy> strategyMap = Map.of(
        TipoColaborador.PADRAO.name(), new ColaboradorPadraoRequestValidatorStrategy(),
        TipoColaborador.COMISSIONADO.name(), new ColaboradorComissionadoRequestValidatorStrategy(),
        TipoColaborador.PRODUCAO.name(), new ColaboradorProducaoRequestValidatorStrategy()
    );

    public void validarDados(ColaboradorRequest request) {
        List<FieldMessage> fieldMessages = new ArrayList<>();
        if (request.getMatricula() == null || request.getMatricula().trim().isEmpty()) {
            fieldMessages.add(new FieldMessage("matricula", "Matrícula é obrigatória"));
        }
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            fieldMessages.add(new FieldMessage("nome", "Nome é obrigatório"));
        }
        if (request.getSalarioBase() == null) {
            fieldMessages.add(new FieldMessage("salarioBase", "Salário base é obrigatório"));
        }
        if (request.getSalarioBase() != null && request.getSalarioBase().compareTo(BigDecimal.ZERO) < 0) {
            fieldMessages.add(new FieldMessage("salarioBase", "Salário base deve ser maior ou igual a zero"));
        }
        if (request.getTipoColaborador() == null || request.getTipoColaborador().trim().isEmpty()) {
            fieldMessages.add(new FieldMessage("tipoColaborador", "Tipo de colaborador é obrigatório"));
        }
        if (request.getTipoColaborador() != null && !request.getTipoColaborador().matches("PADRAO|COMISSIONADO|PRODUCAO")) {
            fieldMessages.add(new FieldMessage("tipoColaborador", "Tipo de colaborador inválido. Valores permitidos: PADRAO, COMISSIONADO, PRODUCAO"));
        }
        if (request.getTipoColaborador() != null && strategyMap.containsKey(request.getTipoColaborador())) {
            strategyMap.get(request.getTipoColaborador()).validarDados(request, fieldMessages);
        }
        if (!fieldMessages.isEmpty()) {
            throw new BadRequestException(fieldMessages);
        }
    }

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
