package com.fapa.back.colaborador.mappers.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorProducaoDetailResponse;
import com.fapa.back.colaborador.domain.ColaboradorProducao;
import com.fapa.back.colaborador.enums.TipoColaborador;

@DisplayName("ColaboradorProducaoMapperStrategy")
class ColaboradorProducaoMapperStrategyTest {

    private final ColaboradorProducaoMapperStrategy strategy = new ColaboradorProducaoMapperStrategy();

    @Test
    @DisplayName("mapearParaEntidade retorna null quando a requisição é nula")
    void mapearParaEntidadeRetornaNullQuandoRequestNulo() {
        assertThat(strategy.mapearParaEntidade(null)).isNull();
    }

    @Test
    @DisplayName("mapearParaEntidade cria ColaboradorProducao com quantidade e valor unitário")
    void mapearParaEntidadeCriaProducao() {
        var entity = strategy.mapearParaEntidade(requestProducao(10, "5.5"));

        assertThat(entity).isInstanceOf(ColaboradorProducao.class);
        ColaboradorProducao producao = (ColaboradorProducao) entity;
        assertThat(producao.getMatricula()).isEqualTo("MAT003");
        assertThat(producao.getNome()).isEqualTo("Carla");
        assertThat(producao.getSalarioBase()).isEqualByComparingTo("2000");
        assertThat(producao.getQuantidadeProduzida()).isEqualTo(10);
        assertThat(producao.getValorUnidadeProduzida()).isEqualByComparingTo("5.5");
        assertThat(producao.getTipoColaborador()).isEqualTo(TipoColaborador.PRODUCAO);
    }

    @Test
    @DisplayName("mapearPropriedades atualiza quantidade e valor unitário da entidade existente")
    void mapearPropriedadesAtualizaValores() {
        ColaboradorProducao existente = new ColaboradorProducao();
        existente.setId(7L);

        strategy.mapearPropriedades(requestProducao(4, "12.25"), existente);

        assertThat(existente.getId()).isEqualTo(7L);
        assertThat(existente.getQuantidadeProduzida()).isEqualTo(4);
        assertThat(existente.getValorUnidadeProduzida()).isEqualByComparingTo("12.25");
        assertThat(existente.getTipoColaborador()).isEqualTo(TipoColaborador.PRODUCAO);
    }

    @Test
    @DisplayName("mapearParaDetailResponse retorna null quando a entidade é nula")
    void mapearParaDetailResponseRetornaNullQuandoEntidadeNula() {
        assertThat(strategy.mapearParaDetailResponse(null)).isNull();
    }

    @Test
    @DisplayName("mapearParaDetailResponse calcula produtividade e salário final com 2 casas")
    void mapearParaDetailResponseCalculaValores() {
        ColaboradorProducaoDetailResponse response =
            (ColaboradorProducaoDetailResponse) strategy.mapearParaDetailResponse(producao(10, "5.5"));

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getQuantidadeProduzida()).isEqualTo(10);
        assertThat(response.getValorUnidadeProduzida()).isEqualTo(new BigDecimal("5.50"));
        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("55.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2055.00"));
        assertThat(response.getTipoColaborador()).isEqualTo(TipoColaborador.PRODUCAO);
    }

    @Test
    @DisplayName("mapearParaDetailResponse arredonda HALF_UP a produtividade fracionada")
    void mapearParaDetailResponseArredonda() {
        // 3 * 0.333 = 0.999 -> 1.00 ; salário final = 2000 + 0.999 = 2000.999 -> 2001.00
        ColaboradorProducaoDetailResponse response =
            (ColaboradorProducaoDetailResponse) strategy.mapearParaDetailResponse(producao(3, "0.333"));

        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("1.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2001.00"));
    }

    private static ColaboradorProducaoRequest requestProducao(Integer quantidade, String valorUnitario) {
        ColaboradorProducaoRequest request = new ColaboradorProducaoRequest();
        request.setMatricula("MAT003");
        request.setNome("Carla");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("PRODUCAO");
        request.setQuantidadeProduzida(quantidade);
        request.setValorUnidadeProduzida(new BigDecimal(valorUnitario));
        return request;
    }

    private static ColaboradorProducao producao(Integer quantidade, String valorUnitario) {
        ColaboradorProducao colaborador = new ColaboradorProducao();
        colaborador.setId(3L);
        colaborador.setMatricula("MAT003");
        colaborador.setNome("Carla");
        colaborador.setSalarioBase(BigDecimal.valueOf(2000));
        colaborador.setTipoColaborador(TipoColaborador.PRODUCAO);
        colaborador.setQuantidadeProduzida(quantidade);
        colaborador.setValorUnidadeProduzida(new BigDecimal(valorUnitario));
        return colaborador;
    }
}