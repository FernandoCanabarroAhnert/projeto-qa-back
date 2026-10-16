package com.fapa.back.colaborador.mappers.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorComissionadoDetailResponse;
import com.fapa.back.colaborador.domain.ColaboradorComissionado;
import com.fapa.back.colaborador.enums.TipoColaborador;

@DisplayName("ColaboradorComissionadoMapperStrategy")
class ColaboradorComissionadoMapperStrategyTest {

    private final ColaboradorComissionadoMapperStrategy strategy = new ColaboradorComissionadoMapperStrategy();

    @Test
    @DisplayName("mapearParaEntidade retorna null quando a requisição é nula")
    void mapearParaEntidadeRetornaNullQuandoRequestNulo() {
        assertThat(strategy.mapearParaEntidade(null)).isNull();
    }

    @Test
    @DisplayName("mapearParaEntidade cria ColaboradorComissionado com vendas e porcentagem")
    void mapearParaEntidadeCriaComissionado() {
        var entity = strategy.mapearParaEntidade(requestComissionado("1000", "10"));

        assertThat(entity).isInstanceOf(ColaboradorComissionado.class);
        ColaboradorComissionado comissionado = (ColaboradorComissionado) entity;
        assertThat(comissionado.getMatricula()).isEqualTo("MAT002");
        assertThat(comissionado.getNome()).isEqualTo("Bruno");
        assertThat(comissionado.getSalarioBase()).isEqualByComparingTo("2000");
        assertThat(comissionado.getValorVendas()).isEqualByComparingTo("1000");
        assertThat(comissionado.getPorcentagemComissao()).isEqualByComparingTo("10");
        assertThat(comissionado.getTipoColaborador()).isEqualTo(TipoColaborador.COMISSIONADO);
    }

    @Test
    @DisplayName("mapearPropriedades atualiza vendas e porcentagem da entidade existente")
    void mapearPropriedadesAtualizaValores() {
        ColaboradorComissionado existente = new ColaboradorComissionado();
        existente.setId(5L);

        strategy.mapearPropriedades(requestComissionado("500", "7"), existente);

        assertThat(existente.getId()).isEqualTo(5L);
        assertThat(existente.getValorVendas()).isEqualByComparingTo("500");
        assertThat(existente.getPorcentagemComissao()).isEqualByComparingTo("7");
    }

    @Test
    @DisplayName("mapearParaDetailResponse retorna null quando a entidade é nula")
    void mapearParaDetailResponseRetornaNullQuandoEntidadeNula() {
        assertThat(strategy.mapearParaDetailResponse(null)).isNull();
    }

    @Test
    @DisplayName("mapearParaDetailResponse calcula adicionais e salário final com 2 casas")
    void mapearParaDetailResponseCalculaValores() {
        ColaboradorComissionadoDetailResponse response =
            (ColaboradorComissionadoDetailResponse) strategy.mapearParaDetailResponse(comissionado("1000", "10"));

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getSalarioBase()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("100.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2100.00"));
        assertThat(response.getValorVendas()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(response.getPorcentagemComissao()).isEqualByComparingTo("10");
        assertThat(response.getTipoColaborador()).isEqualTo(TipoColaborador.COMISSIONADO);
    }

    @Test
    @DisplayName("mapearParaDetailResponse arredonda HALF_UP a comissão com mais de 2 casas")
    void mapearParaDetailResponseArredondaComissao() {
        // 333.33 * 12.5% = 41.66625 -> 41.67 ; salário final = 2000 + 41.66625 = 2041.66625 -> 2041.67
        ColaboradorComissionadoDetailResponse response =
            (ColaboradorComissionadoDetailResponse) strategy.mapearParaDetailResponse(comissionado("333.33", "12.5"));

        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("41.67"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2041.67"));
    }

    private static ColaboradorComissionadoRequest requestComissionado(String valorVendas, String porcentagem) {
        ColaboradorComissionadoRequest request = new ColaboradorComissionadoRequest();
        request.setMatricula("MAT002");
        request.setNome("Bruno");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("COMISSIONADO");
        request.setValorVendas(new BigDecimal(valorVendas));
        request.setPorcentagemComissao(new BigDecimal(porcentagem));
        return request;
    }

    private static ColaboradorComissionado comissionado(String valorVendas, String porcentagem) {
        ColaboradorComissionado colaborador = new ColaboradorComissionado();
        colaborador.setId(2L);
        colaborador.setMatricula("MAT002");
        colaborador.setNome("Bruno");
        colaborador.setSalarioBase(BigDecimal.valueOf(2000));
        colaborador.setTipoColaborador(TipoColaborador.COMISSIONADO);
        colaborador.setValorVendas(new BigDecimal(valorVendas));
        colaborador.setPorcentagemComissao(new BigDecimal(porcentagem));
        return colaborador;
    }
}