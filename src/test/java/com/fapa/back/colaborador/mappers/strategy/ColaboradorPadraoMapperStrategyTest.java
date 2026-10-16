package com.fapa.back.colaborador.mappers.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fapa.back.colaborador.controllers.request.ColaboradorPadraoRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorPadraoDetailResponse;
import com.fapa.back.colaborador.domain.ColaboradorPadrao;
import com.fapa.back.colaborador.enums.TipoColaborador;

@DisplayName("ColaboradorPadraoMapperStrategy")
class ColaboradorPadraoMapperStrategyTest {

    private final ColaboradorPadraoMapperStrategy strategy = new ColaboradorPadraoMapperStrategy();

    @Test
    @DisplayName("mapearParaEntidade retorna null quando a requisição é nula")
    void mapearParaEntidadeRetornaNullQuandoRequestNulo() {
        assertThat(strategy.mapearParaEntidade(null)).isNull();
    }

    @Test
    @DisplayName("mapearParaEntidade cria ColaboradorPadrao com os dados da requisição")
    void mapearParaEntidadeCriaPadrao() {
        var entity = strategy.mapearParaEntidade(requestPadrao());

        assertThat(entity).isInstanceOf(ColaboradorPadrao.class);
        assertThat(entity.getMatricula()).isEqualTo("MAT001");
        assertThat(entity.getNome()).isEqualTo("Ana");
        assertThat(entity.getSalarioBase()).isEqualByComparingTo("2000");
        assertThat(entity.getTipoColaborador()).isEqualTo(TipoColaborador.PADRAO);
    }

    @Test
    @DisplayName("mapearPropriedades atualiza a entidade existente sem alterar o id")
    void mapearPropriedadesAtualizaSemAlterarId() {
        ColaboradorPadrao existente = new ColaboradorPadrao();
        existente.setId(10L);
        existente.setNome("Antigo");

        strategy.mapearPropriedades(requestPadrao(), existente);

        assertThat(existente.getId()).isEqualTo(10L);
        assertThat(existente.getNome()).isEqualTo("Ana");
        assertThat(existente.getMatricula()).isEqualTo("MAT001");
        assertThat(existente.getTipoColaborador()).isEqualTo(TipoColaborador.PADRAO);
    }

    @Test
    @DisplayName("mapearParaDetailResponse retorna null quando a entidade é nula")
    void mapearParaDetailResponseRetornaNullQuandoEntidadeNula() {
        assertThat(strategy.mapearParaDetailResponse(null)).isNull();
    }

    @Test
    @DisplayName("mapearParaDetailResponse formata salários com 2 casas e adicionais zero")
    void mapearParaDetailResponseFormataValores() {
        ColaboradorPadrao padrao = new ColaboradorPadrao();
        padrao.setId(1L);
        padrao.setMatricula("MAT001");
        padrao.setNome("Ana");
        padrao.setSalarioBase(BigDecimal.valueOf(2000));
        padrao.setTipoColaborador(TipoColaborador.PADRAO);

        ColaboradorPadraoDetailResponse response = (ColaboradorPadraoDetailResponse) strategy.mapearParaDetailResponse(padrao);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getMatricula()).isEqualTo("MAT001");
        assertThat(response.getNome()).isEqualTo("Ana");
        assertThat(response.getSalarioBase()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("0.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(response.getTipoColaborador()).isEqualTo(TipoColaborador.PADRAO);
    }

    private static ColaboradorPadraoRequest requestPadrao() {
        ColaboradorPadraoRequest request = new ColaboradorPadraoRequest();
        request.setMatricula("MAT001");
        request.setNome("Ana");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("PADRAO");
        return request;
    }
}