package com.fapa.back.colaborador.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.domain.ColaboradorPadrao;
import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.colaborador.repositories.ColaboradorRepository;
import com.fapa.back.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("BuscarColaboradorPorIdServiceImpl")
class BuscarColaboradorPorIdServiceImplTest {

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @InjectMocks
    private BuscarColaboradorPorIdServiceImpl buscarColaboradorPorIdService;

    @Test
    @DisplayName("retorna o colaborador quando o id existe")
    void retornaColaboradorQuandoExiste() {
        ColaboradorPadrao colaborador = new ColaboradorPadrao();
        colaborador.setId(1L);
        colaborador.setMatricula("MAT0001");
        colaborador.setNome("Carlos Silva");
        colaborador.setSalarioBase(BigDecimal.valueOf(3500));
        colaborador.setTipoColaborador(TipoColaborador.PADRAO);
        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaborador));

        Colaborador encontrado = buscarColaboradorPorIdService.buscarPorId(1L);

        assertThat(encontrado).isSameAs(colaborador);
    }

    @Test
    @DisplayName("lança NotFoundException com mensagem clara quando o id não existe")
    void lancaNotFoundQuandoNaoExiste() {
        when(colaboradorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buscarColaboradorPorIdService.buscarPorId(99L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Colaborador não encontrado com o ID: 99");
    }
}