package com.fapa.back.colaborador.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorPadraoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorComissionadoDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorListResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorPadraoDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorProducaoDetailResponse;
import com.fapa.back.colaborador.domain.ColaboradorComissionado;
import com.fapa.back.colaborador.domain.ColaboradorPadrao;
import com.fapa.back.colaborador.domain.ColaboradorProducao;
import com.fapa.back.colaborador.enums.TipoColaborador;

@DisplayName("ColaboradorMapper")
class ColaboradorMapperTest {

    @Nested
    @DisplayName("toListResponse")
    class ToListResponse {

        @Test
        @DisplayName("retorna null quando o colaborador é nulo")
        void retornaNullQuandoColaboradorNulo() {
            assertThat(ColaboradorMapper.toListResponse(null)).isNull();
        }

        @Test
        @DisplayName("mapeia PADRAO com adicionais zero e salário final igual ao base")
        void mapeiaPadrao() {
            ColaboradorListResponse response = ColaboradorMapper.toListResponse(criarPadrao());

            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getMatricula()).isEqualTo("MAT001");
            assertThat(response.getNome()).isEqualTo("Ana");
            assertThat(response.getSalarioBase()).isEqualTo(new BigDecimal("2000.00"));
            assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("0.00"));
            assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2000.00"));
            assertThat(response.getTipoColaborador()).isEqualTo(TipoColaborador.PADRAO);
        }

        @Test
        @DisplayName("mapeia COMISSIONADO somando a comissão ao salário final")
        void mapeiaComissionado() {
            ColaboradorListResponse response = ColaboradorMapper.toListResponse(criarComissionado());

            assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("100.00"));
            assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2100.00"));
            assertThat(response.getTipoColaborador()).isEqualTo(TipoColaborador.COMISSIONADO);
        }

        @Test
        @DisplayName("mapeia PRODUCAO somando a produtividade ao salário final")
        void mapeiaProducao() {
            ColaboradorListResponse response = ColaboradorMapper.toListResponse(criarProducao());

            assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("55.00"));
            assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2055.00"));
            assertThat(response.getTipoColaborador()).isEqualTo(TipoColaborador.PRODUCAO);
        }
    }

    @Nested
    @DisplayName("toDetailResponse")
    class ToDetailResponse {

        @Test
        @DisplayName("retorna null quando o colaborador é nulo")
        void retornaNullQuandoColaboradorNulo() {
            assertThat(ColaboradorMapper.toDetailResponse(null)).isNull();
        }

        @Test
        @DisplayName("delega para a strategy de PADRAO")
        void delegaParaPadrao() {
            var response = ColaboradorMapper.toDetailResponse(criarPadrao());

            assertThat(response).isInstanceOf(ColaboradorPadraoDetailResponse.class);
            assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("2000.00"));
        }

        @Test
        @DisplayName("delega para a strategy de COMISSIONADO")
        void delegaParaComissionado() {
            var response = ColaboradorMapper.toDetailResponse(criarComissionado());

            assertThat(response).isInstanceOf(ColaboradorComissionadoDetailResponse.class);
            ColaboradorComissionadoDetailResponse comissionado = (ColaboradorComissionadoDetailResponse) response;
            assertThat(comissionado.getValorVendas()).isEqualTo(new BigDecimal("1000.00"));
            assertThat(comissionado.getAdicionais()).isEqualTo(new BigDecimal("100.00"));
        }

        @Test
        @DisplayName("delega para a strategy de PRODUCAO")
        void delegaParaProducao() {
            var response = ColaboradorMapper.toDetailResponse(criarProducao());

            assertThat(response).isInstanceOf(ColaboradorProducaoDetailResponse.class);
            ColaboradorProducaoDetailResponse producao = (ColaboradorProducaoDetailResponse) response;
            assertThat(producao.getQuantidadeProduzida()).isEqualTo(10);
            assertThat(producao.getValorUnidadeProduzida()).isEqualTo(new BigDecimal("5.50"));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        @DisplayName("retorna null quando a requisição é nula")
        void retornaNullQuandoRequestNulo() {
            assertThat(ColaboradorMapper.toEntity(null)).isNull();
        }

        @Test
        @DisplayName("cria ColaboradorPadrao para tipo PADRAO")
        void criaPadrao() {
            var entity = ColaboradorMapper.toEntity(requestPadrao());

            assertThat(entity).isInstanceOf(ColaboradorPadrao.class);
            assertThat(entity.getMatricula()).isEqualTo("MAT001");
            assertThat(entity.getNome()).isEqualTo("Ana");
            assertThat(entity.getSalarioBase()).isEqualByComparingTo("2000");
            assertThat(entity.getTipoColaborador()).isEqualTo(TipoColaborador.PADRAO);
        }

        @Test
        @DisplayName("cria ColaboradorComissionado com valores de vendas e comissão")
        void criaComissionado() {
            var entity = ColaboradorMapper.toEntity(requestComissionado());

            assertThat(entity).isInstanceOf(ColaboradorComissionado.class);
            ColaboradorComissionado comissionado = (ColaboradorComissionado) entity;
            assertThat(comissionado.getValorVendas()).isEqualByComparingTo("1000");
            assertThat(comissionado.getPorcentagemComissao()).isEqualByComparingTo("10");
            assertThat(comissionado.getTipoColaborador()).isEqualTo(TipoColaborador.COMISSIONADO);
        }

        @Test
        @DisplayName("cria ColaboradorProducao com quantidade e valor unitário")
        void criaProducao() {
            var entity = ColaboradorMapper.toEntity(requestProducao());

            assertThat(entity).isInstanceOf(ColaboradorProducao.class);
            ColaboradorProducao producao = (ColaboradorProducao) entity;
            assertThat(producao.getQuantidadeProduzida()).isEqualTo(10);
            assertThat(producao.getValorUnidadeProduzida()).isEqualByComparingTo("5.5");
            assertThat(producao.getTipoColaborador()).isEqualTo(TipoColaborador.PRODUCAO);
        }

        @Test
        @DisplayName("lança IllegalArgumentException para tipo desconhecido")
        void lancaExcecaoParaTipoDesconhecido() {
            ColaboradorPadraoRequest request = requestPadrao();
            request.setTipoColaborador("XPTO");

            assertThatThrownBy(() -> ColaboradorMapper.toEntity(request))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("atualizarEntidade")
    class AtualizarEntidade {

        @Test
        @DisplayName("não faz nada quando a entidade existente é nula")
        void ignoraEntidadeNula() {
            assertThatCode(() -> ColaboradorMapper.atualizarEntidade(null, requestPadrao()))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("não altera a entidade quando a requisição é nula")
        void ignoraRequestNulo() {
            ColaboradorPadrao existente = criarPadrao();

            ColaboradorMapper.atualizarEntidade(existente, null);

            assertThat(existente.getNome()).isEqualTo("Ana");
        }

        @Test
        @DisplayName("atualiza os campos de COMISSIONADO in-place, mantendo o id")
        void atualizaComissionadoEmSeuLugar() {
            ColaboradorComissionado existente = criarComissionado();
            ColaboradorComissionadoRequest request = requestComissionado();
            request.setMatricula("MAT999");
            request.setNome("Novo Nome");
            request.setSalarioBase(BigDecimal.valueOf(3000));
            request.setValorVendas(BigDecimal.valueOf(2000));
            request.setPorcentagemComissao(BigDecimal.valueOf(5));

            ColaboradorMapper.atualizarEntidade(existente, request);

            assertThat(existente.getId()).isEqualTo(2L);
            assertThat(existente.getMatricula()).isEqualTo("MAT999");
            assertThat(existente.getNome()).isEqualTo("Novo Nome");
            assertThat(existente.getSalarioBase()).isEqualByComparingTo("3000");
            assertThat(existente.getValorVendas()).isEqualByComparingTo("2000");
            assertThat(existente.getPorcentagemComissao()).isEqualByComparingTo("5");
        }
    }

    private static ColaboradorPadrao criarPadrao() {
        ColaboradorPadrao colaborador = new ColaboradorPadrao();
        colaborador.setId(1L);
        colaborador.setMatricula("MAT001");
        colaborador.setNome("Ana");
        colaborador.setSalarioBase(BigDecimal.valueOf(2000));
        colaborador.setTipoColaborador(TipoColaborador.PADRAO);
        return colaborador;
    }

    private static ColaboradorComissionado criarComissionado() {
        ColaboradorComissionado colaborador = new ColaboradorComissionado();
        colaborador.setId(2L);
        colaborador.setMatricula("MAT002");
        colaborador.setNome("Bruno");
        colaborador.setSalarioBase(BigDecimal.valueOf(2000));
        colaborador.setTipoColaborador(TipoColaborador.COMISSIONADO);
        colaborador.setValorVendas(BigDecimal.valueOf(1000));
        colaborador.setPorcentagemComissao(BigDecimal.valueOf(10));
        return colaborador;
    }

    private static ColaboradorProducao criarProducao() {
        ColaboradorProducao colaborador = new ColaboradorProducao();
        colaborador.setId(3L);
        colaborador.setMatricula("MAT003");
        colaborador.setNome("Carla");
        colaborador.setSalarioBase(BigDecimal.valueOf(2000));
        colaborador.setTipoColaborador(TipoColaborador.PRODUCAO);
        colaborador.setQuantidadeProduzida(10);
        colaborador.setValorUnidadeProduzida(new BigDecimal("5.5"));
        return colaborador;
    }

    private static ColaboradorPadraoRequest requestPadrao() {
        ColaboradorPadraoRequest request = new ColaboradorPadraoRequest();
        request.setMatricula("MAT001");
        request.setNome("Ana");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("PADRAO");
        return request;
    }

    private static ColaboradorComissionadoRequest requestComissionado() {
        ColaboradorComissionadoRequest request = new ColaboradorComissionadoRequest();
        request.setMatricula("MAT002");
        request.setNome("Bruno");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("COMISSIONADO");
        request.setValorVendas(BigDecimal.valueOf(1000));
        request.setPorcentagemComissao(BigDecimal.valueOf(10));
        return request;
    }

    private static ColaboradorProducaoRequest requestProducao() {
        ColaboradorProducaoRequest request = new ColaboradorProducaoRequest();
        request.setMatricula("MAT003");
        request.setNome("Carla");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("PRODUCAO");
        request.setQuantidadeProduzida(10);
        request.setValorUnidadeProduzida(new BigDecimal("5.5"));
        return request;
    }
}