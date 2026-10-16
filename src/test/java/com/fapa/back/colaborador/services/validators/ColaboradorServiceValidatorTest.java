package com.fapa.back.colaborador.services.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorPadraoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.domain.ColaboradorPadrao;
import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.colaborador.repositories.ColaboradorRepository;
import com.fapa.back.shared.dtos.FieldMessage;
import com.fapa.back.shared.exceptions.BadRequestException;
import com.fapa.back.shared.exceptions.ConflictException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ColaboradorServiceValidator")
class ColaboradorServiceValidatorTest {

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @InjectMocks
    private ColaboradorServiceValidator validator;

    @Nested
    @DisplayName("validarDados")
    class ValidarDados {

        @Test
        @DisplayName("aceita uma requisição PADRAO válida")
        void aceitaPadraoValido() {
            assertThatCode(() -> validator.validarDados(requestPadraoValido()))
                .doesNotThrowAnyException();
            verifyNoInteractions(colaboradorRepository);
        }

        @Test
        @DisplayName("aceita uma requisição COMISSIONADO válida")
        void aceitaComissionadoValido() {
            assertThatCode(() -> validator.validarDados(requestComissionadoValido()))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("aceita uma requisição PRODUCAO válida")
        void aceitaProducaoValido() {
            assertThatCode(() -> validator.validarDados(requestProducaoValido()))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("aceita salário base igual a zero")
        void aceitaSalarioBaseZero() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setSalarioBase(BigDecimal.ZERO);

            assertThatCode(() -> validator.validarDados(request)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("rejeita matrícula nula")
        void rejeitaMatriculaNula() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setMatricula(null);

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("matricula", "Matrícula é obrigatória"));
        }

        @Test
        @DisplayName("rejeita matrícula em branco")
        void rejeitaMatriculaEmBranco() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setMatricula("   ");

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("matricula", "Matrícula é obrigatória"));
        }

        @Test
        @DisplayName("rejeita nome vazio")
        void rejeitaNomeVazio() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setNome("");

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("nome", "Nome é obrigatório"));
        }

        @Test
        @DisplayName("rejeita salário base nulo")
        void rejeitaSalarioBaseNulo() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setSalarioBase(null);

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("salarioBase", "Salário base é obrigatório"));
        }

        @Test
        @DisplayName("rejeita salário base negativo")
        void rejeitaSalarioBaseNegativo() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setSalarioBase(new BigDecimal("-1"));

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("salarioBase", "Salário base deve ser maior ou igual a zero"));
        }

        @Test
        @DisplayName("rejeita tipo de colaborador nulo com BadRequestException")
        void rejeitaTipoNulo() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setTipoColaborador(null);

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("tipoColaborador", "Tipo de colaborador é obrigatório"));
        }

        @Test
        @DisplayName("rejeita tipo de colaborador inválido com BadRequestException")
        void rejeitaTipoInvalido() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setTipoColaborador("XPTO");

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("tipoColaborador",
                    "Tipo de colaborador inválido. Valores permitidos: PADRAO, COMISSIONADO, PRODUCAO"));
        }

        @Test
        @DisplayName("acumula os erros de todos os campos base em uma única exceção")
        void acumulaErrosDosCamposBase() {
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setMatricula(null);
            request.setNome(" ");
            request.setSalarioBase(null);

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(
                    tuple("matricula", "Matrícula é obrigatória"),
                    tuple("nome", "Nome é obrigatório"),
                    tuple("salarioBase", "Salário base é obrigatório"));
        }

        @Test
        @DisplayName("delega a validação específica de COMISSIONADO para a strategy")
        void delegaValidacaoComissionado() {
            ColaboradorComissionadoRequest request = requestComissionadoValido();
            request.setValorVendas(new BigDecimal("-1"));

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("valorVendas", "Valor das vendas deve ser maior ou igual a zero"));
        }

        @Test
        @DisplayName("delega a validação específica de PRODUCAO para a strategy")
        void delegaValidacaoProducao() {
            ColaboradorProducaoRequest request = requestProducaoValido();
            request.setQuantidadeProduzida(null);

            assertThat(mensagensDe(request))
                .extracting(FieldMessage::getFieldName, FieldMessage::getMessage)
                .containsExactly(tuple("quantidadeProduzida", "Quantidade produzida é obrigatório"));
        }
    }

    @Nested
    @DisplayName("validarMatriculaExistente")
    class ValidarMatriculaExistente {

        @Test
        @DisplayName("lança ConflictException quando a matrícula já existe")
        void lancaConflictQuandoMatriculaExiste() {
            when(colaboradorRepository.existsByMatricula("MAT001")).thenReturn(true);

            assertThatThrownBy(() -> validator.validarMatriculaExistente("MAT001"))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Colaborador com a matrícula MAT001 já existe.");
        }

        @Test
        @DisplayName("não lança exceção quando a matrícula está livre")
        void naoLancaQuandoMatriculaLivre() {
            when(colaboradorRepository.existsByMatricula("MAT001")).thenReturn(false);

            assertThatCode(() -> validator.validarMatriculaExistente("MAT001"))
                .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("validarMatriculaExistenteParaAtualizacao")
    class ValidarMatriculaExistenteParaAtualizacao {

        @Test
        @DisplayName("não consulta o repositório quando a matrícula não mudou")
        void naoConsultaQuandoMatriculaNaoMudou() {
            ColaboradorPadrao existente = colaboradorComMatricula("MAT001");
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setMatricula("MAT001");

            assertThatCode(() -> validator.validarMatriculaExistenteParaAtualizacao("MAT001", existente, request))
                .doesNotThrowAnyException();
            verify(colaboradorRepository, never()).existsByMatricula(anyString());
        }

        @Test
        @DisplayName("lança ConflictException quando a nova matrícula já pertence a outro colaborador")
        void lancaConflictQuandoNovaMatriculaExiste() {
            ColaboradorPadrao existente = colaboradorComMatricula("MAT001");
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setMatricula("MAT002");
            when(colaboradorRepository.existsByMatricula("MAT002")).thenReturn(true);

            assertThatThrownBy(() -> validator.validarMatriculaExistenteParaAtualizacao("MAT001", existente, request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Colaborador com a matrícula MAT002 já existe.");
        }

        @Test
        @DisplayName("não lança exceção quando a nova matrícula está livre")
        void naoLancaQuandoNovaMatriculaLivre() {
            ColaboradorPadrao existente = colaboradorComMatricula("MAT001");
            ColaboradorPadraoRequest request = requestPadraoValido();
            request.setMatricula("MAT002");
            when(colaboradorRepository.existsByMatricula("MAT002")).thenReturn(false);

            assertThatCode(() -> validator.validarMatriculaExistenteParaAtualizacao("MAT001", existente, request))
                .doesNotThrowAnyException();
            verify(colaboradorRepository).existsByMatricula("MAT002");
        }
    }

    private List<FieldMessage> mensagensDe(ColaboradorRequest request) {
        Throwable erro = catchThrowable(() -> validator.validarDados(request));
        assertThat(erro).isInstanceOf(BadRequestException.class);
        return ((BadRequestException) erro).getFieldMessages();
    }

    private static ColaboradorPadrao colaboradorComMatricula(String matricula) {
        ColaboradorPadrao colaborador = new ColaboradorPadrao();
        colaborador.setMatricula(matricula);
        colaborador.setTipoColaborador(TipoColaborador.PADRAO);
        return colaborador;
    }

    private static ColaboradorPadraoRequest requestPadraoValido() {
        ColaboradorPadraoRequest request = new ColaboradorPadraoRequest();
        request.setMatricula("MAT001");
        request.setNome("Ana");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("PADRAO");
        return request;
    }

    private static ColaboradorComissionadoRequest requestComissionadoValido() {
        ColaboradorComissionadoRequest request = new ColaboradorComissionadoRequest();
        request.setMatricula("MAT002");
        request.setNome("Bruno");
        request.setSalarioBase(BigDecimal.valueOf(2000));
        request.setTipoColaborador("COMISSIONADO");
        request.setValorVendas(BigDecimal.valueOf(1000));
        request.setPorcentagemComissao(BigDecimal.TEN);
        return request;
    }

    private static ColaboradorProducaoRequest requestProducaoValido() {
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