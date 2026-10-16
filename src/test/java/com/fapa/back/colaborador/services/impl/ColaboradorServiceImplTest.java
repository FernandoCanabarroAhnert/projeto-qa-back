package com.fapa.back.colaborador.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorPadraoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorListResponse;
import com.fapa.back.colaborador.controllers.response.ResumoFolhaPagamentoResponse;
import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.domain.ColaboradorComissionado;
import com.fapa.back.colaborador.domain.ColaboradorPadrao;
import com.fapa.back.colaborador.domain.ColaboradorProducao;
import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.colaborador.projections.ResumoFolhaPagamentoProjection;
import com.fapa.back.colaborador.repositories.ColaboradorRepository;
import com.fapa.back.colaborador.services.BuscarColaboradorPorIdService;
import com.fapa.back.colaborador.services.validators.ColaboradorServiceValidator;
import com.fapa.back.shared.exceptions.BadRequestException;
import com.fapa.back.shared.exceptions.ConflictException;
import com.fapa.back.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ColaboradorServiceImpl")
class ColaboradorServiceImplTest {

    @InjectMocks
    private ColaboradorServiceImpl colaboradorService;
    @Mock
    private ColaboradorRepository colaboradorRepository;
    @Mock
    private BuscarColaboradorPorIdService buscarColaboradorPorIdService;
    @Mock
    private ColaboradorServiceValidator colaboradorServiceValidator;

    private ColaboradorPadrao colaboradorPadraoEntity;
    private ColaboradorComissionado colaboradorComissionadoEntity;
    private ColaboradorProducao colaboradorProducaoEntity;
    private ColaboradorPadraoRequest colaboradorPadraoRequest;
    private ColaboradorComissionadoRequest colaboradorComissionadoRequest;
    private ColaboradorProducaoRequest colaboradorProducaoRequest;

    @BeforeEach
    void setup() {
        colaboradorPadraoEntity = new ColaboradorPadrao();
        colaboradorPadraoEntity.setMatricula("MAT0001");
        colaboradorPadraoEntity.setNome("Carlos Silva");
        colaboradorPadraoEntity.setSalarioBase(BigDecimal.valueOf(3500.00));
        colaboradorPadraoEntity.setTipoColaborador(TipoColaborador.PADRAO);

        colaboradorComissionadoEntity = new ColaboradorComissionado();
        colaboradorComissionadoEntity.setMatricula("MAT0002");
        colaboradorComissionadoEntity.setNome("Ana Souza");
        colaboradorComissionadoEntity.setSalarioBase(BigDecimal.valueOf(3000.00));
        colaboradorComissionadoEntity.setTipoColaborador(TipoColaborador.COMISSIONADO);
        colaboradorComissionadoEntity.setValorVendas(BigDecimal.valueOf(15000.00));
        colaboradorComissionadoEntity.setPorcentagemComissao(BigDecimal.valueOf(10.00));

        colaboradorProducaoEntity = new ColaboradorProducao();
        colaboradorProducaoEntity.setMatricula("MAT0003");
        colaboradorProducaoEntity.setNome("Bruno Lima");
        colaboradorProducaoEntity.setSalarioBase(BigDecimal.valueOf(2500.00));
        colaboradorProducaoEntity.setTipoColaborador(TipoColaborador.PRODUCAO);
        colaboradorProducaoEntity.setQuantidadeProduzida(100);
        colaboradorProducaoEntity.setValorUnidadeProduzida(BigDecimal.valueOf(20.00));

        colaboradorPadraoRequest = new ColaboradorPadraoRequest();
        colaboradorPadraoRequest.setMatricula("MAT0001");
        colaboradorPadraoRequest.setNome("Carlos Silva");
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.valueOf(3500.00));
        colaboradorPadraoRequest.setTipoColaborador("PADRAO");

        colaboradorComissionadoRequest = new ColaboradorComissionadoRequest();
        colaboradorComissionadoRequest.setMatricula("MAT0002");
        colaboradorComissionadoRequest.setNome("Ana Souza");
        colaboradorComissionadoRequest.setSalarioBase(BigDecimal.valueOf(3000.00));
        colaboradorComissionadoRequest.setTipoColaborador("COMISSIONADO");
        colaboradorComissionadoRequest.setValorVendas(BigDecimal.valueOf(15000.00));
        colaboradorComissionadoRequest.setPorcentagemComissao(BigDecimal.valueOf(10.00));

        colaboradorProducaoRequest = new ColaboradorProducaoRequest();
        colaboradorProducaoRequest.setMatricula("MAT0003");
        colaboradorProducaoRequest.setNome("Bruno Lima");
        colaboradorProducaoRequest.setSalarioBase(BigDecimal.valueOf(2500.00));
        colaboradorProducaoRequest.setTipoColaborador("PRODUCAO");
        colaboradorProducaoRequest.setQuantidadeProduzida(100);
        colaboradorProducaoRequest.setValorUnidadeProduzida(BigDecimal.valueOf(20.00));
    }

    @Test
    @DisplayName("CT-001 - Cadastrar Colaborador Padrão com dados válidos")
    void deveCadastrarColaboradorPadraoComDadosValidos() {
        when(colaboradorRepository.save(any(ColaboradorPadrao.class))).thenReturn(colaboradorPadraoEntity);

        ColaboradorDetailResponse response = colaboradorService.criar(colaboradorPadraoRequest);

        assertThat(response).isNotNull();
        assertThat(response.getMatricula()).isEqualTo(colaboradorPadraoEntity.getMatricula());
        assertThat(response.getNome()).isEqualTo(colaboradorPadraoEntity.getNome());
        assertThat(response.getSalarioBase()).isEqualByComparingTo(colaboradorPadraoEntity.getSalarioBase());
        assertThat(response.getTipoColaborador()).isEqualTo(colaboradorPadraoEntity.getTipoColaborador());
        assertThat(response.getSalarioFinal()).isEqualByComparingTo(colaboradorPadraoEntity.calcularSalarioFinal());
    }

    @Test
    @DisplayName("CT-002 - Cadastrar Colaborador Comissionado com dados válidos")
    void deveCadastrarColaboradorComissionadoComDadosValidos() {
        when(colaboradorRepository.save(any(ColaboradorComissionado.class))).thenReturn(colaboradorComissionadoEntity);

        ColaboradorDetailResponse response = colaboradorService.criar(colaboradorComissionadoRequest);

        assertThat(response).isNotNull();
        assertThat(response.getMatricula()).isEqualTo(colaboradorComissionadoEntity.getMatricula());
        assertThat(response.getNome()).isEqualTo(colaboradorComissionadoEntity.getNome());
        assertThat(response.getSalarioBase()).isEqualByComparingTo(colaboradorComissionadoEntity.getSalarioBase());
        assertThat(response.getTipoColaborador()).isEqualTo(colaboradorComissionadoEntity.getTipoColaborador());
        assertThat(response.getSalarioFinal()).isEqualByComparingTo(colaboradorComissionadoEntity.calcularSalarioFinal());
    }

    @Test
    @DisplayName("CT-003 - Cadastrar Colaborador por Produção com dados válidos")
    void deveCadastrarColaboradorProducaoComDadosValidos() {
        when(colaboradorRepository.save(any(ColaboradorProducao.class))).thenReturn(colaboradorProducaoEntity);

        ColaboradorDetailResponse response = colaboradorService.criar(colaboradorProducaoRequest);

        assertThat(response).isNotNull();
        assertThat(response.getMatricula()).isEqualTo(colaboradorProducaoEntity.getMatricula());
        assertThat(response.getNome()).isEqualTo(colaboradorProducaoEntity.getNome());
        assertThat(response.getSalarioBase()).isEqualByComparingTo(colaboradorProducaoEntity.getSalarioBase());
        assertThat(response.getTipoColaborador()).isEqualTo(colaboradorProducaoEntity.getTipoColaborador());
        assertThat(response.getSalarioFinal()).isEqualByComparingTo(colaboradorProducaoEntity.calcularSalarioFinal());
    }

    @Test
    @DisplayName("CT-004 - Impedir cadastro com matrícula duplicada (RN001)")
    void deveImpedirCadastroComMatriculaDuplicada() {
        doThrow(new ConflictException("Matrícula já cadastrada"))
            .when(colaboradorServiceValidator).validarMatriculaExistente(colaboradorPadraoRequest.getMatricula());

        assertThatThrownBy(() -> colaboradorService.criar(colaboradorPadraoRequest))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("Matrícula já cadastrada");
        verify(colaboradorRepository, never()).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("CT-005 - Impedir cadastro com nome em branco (RN002)")
    void deveImpedirCadastroComNomeEmBranco() {
        colaboradorPadraoRequest.setNome("");
        doThrow(new BadRequestException("Nome não pode ser vazio"))
            .when(colaboradorServiceValidator).validarDados(colaboradorPadraoRequest);

        assertThatThrownBy(() -> colaboradorService.criar(colaboradorPadraoRequest))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Nome não pode ser vazio");
        verify(colaboradorRepository, never()).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("CT-006 - Impedir cadastro com salário base negativo (RN003)")
    void deveImpedirCadastroComSalarioBaseNegativo() {
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.valueOf(-1000.00));
        doThrow(new BadRequestException("Salário base não pode ser negativo"))
            .when(colaboradorServiceValidator).validarDados(colaboradorPadraoRequest);

        assertThatThrownBy(() -> colaboradorService.criar(colaboradorPadraoRequest))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Salário base não pode ser negativo");
    }

    @Test
    @DisplayName("CT-007 - Impedir cadastro com valor de vendas negativo (RN004)")
    void deveImpedirCadastroComValorVendasNegativo() {
        colaboradorComissionadoRequest.setValorVendas(BigDecimal.valueOf(-1000.00));
        doThrow(new BadRequestException("Valor de vendas não pode ser negativo"))
            .when(colaboradorServiceValidator).validarDados(colaboradorComissionadoRequest);

        assertThatThrownBy(() -> colaboradorService.criar(colaboradorComissionadoRequest))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Valor de vendas não pode ser negativo");
    }

    @Test
    @DisplayName("CT-008 - Impedir cadastro com percentual de comissão negativo (RN005)")
    void deveImpedirCadastroComPercentualComissaoNegativo() {
        colaboradorComissionadoRequest.setPorcentagemComissao(BigDecimal.valueOf(-5.00));
        doThrow(new BadRequestException("Percentual de comissão não pode ser negativo"))
            .when(colaboradorServiceValidator).validarDados(colaboradorComissionadoRequest);

        assertThatThrownBy(() -> colaboradorService.criar(colaboradorComissionadoRequest))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Percentual de comissão não pode ser negativo");
    }

    @Test
    @DisplayName("CT-009 - Impedir cadastro com quantidade produzida negativa (RN006)")
    void deveImpedirCadastroComQuantidadeProduzidaNegativa() {
        colaboradorProducaoRequest.setQuantidadeProduzida(-100);
        doThrow(new BadRequestException("Quantidade produzida não pode ser negativa"))
            .when(colaboradorServiceValidator).validarDados(colaboradorProducaoRequest);

        assertThatThrownBy(() -> colaboradorService.criar(colaboradorProducaoRequest))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Quantidade produzida não pode ser negativa");
    }

    @Test
    @DisplayName("CT-010 - Impedir cadastro com valor por unidade negativo (RN007)")
    void deveImpedirCadastroComValorPorUnidadeNegativo() {
        colaboradorProducaoRequest.setValorUnidadeProduzida(BigDecimal.valueOf(-10.00));
        doThrow(new BadRequestException("Valor por unidade não pode ser negativo"))
            .when(colaboradorServiceValidator).validarDados(colaboradorProducaoRequest);

        assertThatThrownBy(() -> colaboradorService.criar(colaboradorProducaoRequest))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Valor por unidade não pode ser negativo");
    }

    @Test
    @DisplayName("CT-011 - Consultar lista de colaboradores cadastrados (RF005)")
    void deveConsultarListaDeColaboradoresCadastrados() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Colaborador> colaboradores = List.of(colaboradorPadraoEntity, colaboradorComissionadoEntity);
        when(colaboradorRepository.findByFiltro(eq(""), isNull(), any(Pageable.class)))
            .thenReturn(new PageImpl<>(colaboradores, pageable, 2));

        Page<ColaboradorListResponse> pagina = colaboradorService.listar(0, 10, "id", "asc", "", null);

        assertThat(pagina.getTotalElements()).isEqualTo(2);
        assertThat(pagina.getContent())
            .extracting(ColaboradorListResponse::getMatricula)
            .containsExactly("MAT0001", "MAT0002");
        assertThat(pagina.getContent().get(1).getSalarioFinal()).isEqualTo(new BigDecimal("4500.00"));
    }

    @Test
    @DisplayName("CT-011 - Listagem aplica o filtro de tipo e a ordenação solicitados")
    void deveListarFiltrandoPorTipoEOrdenando() {
        List<Colaborador> colaboradores = List.of(colaboradorComissionadoEntity);
        when(colaboradorRepository.findByFiltro(eq("Ana"), eq(TipoColaborador.COMISSIONADO), any(Pageable.class)))
            .thenReturn(new PageImpl<>(colaboradores));

        Page<ColaboradorListResponse> pagina = colaboradorService.listar(0, 5, "nome", "desc", "Ana", "COMISSIONADO");

        assertThat(pagina.getContent()).hasSize(1);
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(colaboradorRepository).findByFiltro(eq("Ana"), eq(TipoColaborador.COMISSIONADO), captor.capture());
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
        assertThat(captor.getValue().getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "nome"));
    }

    @Test
    @DisplayName("CT-012 - Calcular salário correto para Colaborador Padrão (RF006)")
    void deveCalcularSalarioCorretoParaColaboradorPadrao() {
        when(buscarColaboradorPorIdService.buscarPorId(1L)).thenReturn(colaboradorPadraoEntity);

        ColaboradorDetailResponse response = colaboradorService.obterPorId(1L);

        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("0.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("3500.00"));
    }

    @Test
    @DisplayName("CT-013 - Calcular salário correto para Colaborador Comissionado (RF006)")
    void deveCalcularSalarioCorretoParaColaboradorComissionado() {
        // Comissão = 15000 x 10% = 1500 ; Salário final = 3000 + 1500 = 4500
        when(buscarColaboradorPorIdService.buscarPorId(2L)).thenReturn(colaboradorComissionadoEntity);

        ColaboradorDetailResponse response = colaboradorService.obterPorId(2L);

        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("1500.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("4500.00"));
    }

    @Test
    @DisplayName("CT-014 - Calcular salário correto para Colaborador por Produção (RF006)")
    void deveCalcularSalarioCorretoParaColaboradorPorProducao() {
        // Produtividade = 100 x 20 = 2000 ; Salário final = 2500 + 2000 = 4500
        when(buscarColaboradorPorIdService.buscarPorId(3L)).thenReturn(colaboradorProducaoEntity);

        ColaboradorDetailResponse response = colaboradorService.obterPorId(3L);

        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("4500.00"));
    }

    @Test
    @DisplayName("CT-016 - Emitir Resumo da Folha com somatório correto (RF008)")
    void deveEmitirResumoDaFolhaComSomatorioCorreto() {
        ResumoFolhaPagamentoProjection total = projecao("TOTAL", 3L, 9000.0);
        ResumoFolhaPagamentoProjection padrao = projecao("PADRAO", 1L, 3500.0);
        ResumoFolhaPagamentoProjection comissionado = projecao("COMISSIONADO", 1L, 4500.0);
        ResumoFolhaPagamentoProjection producao = projecao("PRODUCAO", 1L, 1000.005);
        when(colaboradorRepository.getResumoFolhaPagamento())
            .thenReturn(List.of(total, padrao, comissionado, producao));

        List<ResumoFolhaPagamentoResponse> resumo = colaboradorService.gerarResumoFolhaPagamento();

        assertThat(resumo)
            .extracting(ResumoFolhaPagamentoResponse::getTipo,
                        ResumoFolhaPagamentoResponse::getQuantidade,
                        ResumoFolhaPagamentoResponse::getValorTotal)
            .containsExactly(
                tuple("TOTAL", 3L, new BigDecimal("9000.00")),
                tuple("PADRAO", 1L, new BigDecimal("3500.00")),
                tuple("COMISSIONADO", 1L, new BigDecimal("4500.00")),
                tuple("PRODUCAO", 1L, new BigDecimal("1000.01")));
    }

    @Test
    @DisplayName("Resumo da folha retorna lista vazia quando não há colaboradores")
    void resumoVazioQuandoNaoHaColaboradores() {
        when(colaboradorRepository.getResumoFolhaPagamento()).thenReturn(List.of());

        assertThat(colaboradorService.gerarResumoFolhaPagamento()).isEmpty();
    }

    @Test
    @DisplayName("CT-017 - Alterar salário base de um colaborador existente (RF009)")
    void deveAlterarSalarioBaseDeUmColaboradorExistente() {
        when(buscarColaboradorPorIdService.buscarPorId(1L)).thenReturn(colaboradorPadraoEntity);
        when(colaboradorRepository.save(any(Colaborador.class))).thenAnswer(inv -> inv.getArgument(0));
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.valueOf(4000.00));

        ColaboradorDetailResponse response = colaboradorService.atualizar(1L, colaboradorPadraoRequest);

        assertThat(response.getSalarioBase()).isEqualTo(new BigDecimal("4000.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("4000.00"));
        assertThat(colaboradorPadraoEntity.getAtualizadoEm()).isNotNull();
        verify(colaboradorRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Alterar o tipo de colaborador remove o registro antigo e recria preservando criadoEm")
    void alterarTipoRecriaRegistroPreservandoCriadoEm() {
        LocalDateTime criadoEm = LocalDateTime.of(2025, 1, 10, 9, 0);
        colaboradorComissionadoEntity.setCriadoEm(criadoEm);
        when(buscarColaboradorPorIdService.buscarPorId(2L)).thenReturn(colaboradorComissionadoEntity);
        when(colaboradorRepository.save(any(Colaborador.class))).thenAnswer(inv -> inv.getArgument(0));

        ColaboradorDetailResponse response = colaboradorService.atualizar(2L, colaboradorPadraoRequest);

        verify(colaboradorRepository).deleteById(2L);
        ArgumentCaptor<Colaborador> captor = ArgumentCaptor.forClass(Colaborador.class);
        verify(colaboradorRepository).save(captor.capture());
        Colaborador salvo = captor.getValue();
        assertThat(salvo).isInstanceOf(ColaboradorPadrao.class);
        assertThat(salvo.getCriadoEm()).isEqualTo(criadoEm);
        assertThat(salvo.getAtualizadoEm()).isNotNull();
        assertThat(response.getTipoColaborador()).isEqualTo(TipoColaborador.PADRAO);
    }

    @Test
    @DisplayName("Atualizar com matrícula de outro colaborador não persiste nada")
    void atualizarComMatriculaDeOutroColaboradorNaoPersiste() {
        when(buscarColaboradorPorIdService.buscarPorId(1L)).thenReturn(colaboradorPadraoEntity);
        doThrow(new ConflictException("Colaborador com a matrícula MAT0002 já existe."))
            .when(colaboradorServiceValidator)
            .validarMatriculaExistenteParaAtualizacao(anyString(), any(Colaborador.class), any(ColaboradorRequest.class));
        colaboradorPadraoRequest.setMatricula("MAT0002");

        assertThatThrownBy(() -> colaboradorService.atualizar(1L, colaboradorPadraoRequest))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("MAT0002");
        verify(colaboradorRepository, never()).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("CT-018 - Excluir um colaborador do sistema (RF009)")
    void deveExcluirUmColaboradorDoSistema() {
        when(buscarColaboradorPorIdService.buscarPorId(1L)).thenReturn(colaboradorPadraoEntity);

        colaboradorService.deletar(1L);

        verify(colaboradorRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Excluir colaborador inexistente lança NotFoundException e não remove nada")
    void excluirInexistenteLancaNotFound() {
        when(buscarColaboradorPorIdService.buscarPorId(99L))
            .thenThrow(new NotFoundException("Colaborador não encontrado com o ID: 99"));

        assertThatThrownBy(() -> colaboradorService.deletar(99L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("ID: 99");
        verify(colaboradorRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("CT-019 - Permitir cadastro com Salário Base igual a zero (Limite inferior de RN003)")
    void devePermitirCadastroComSalarioBaseIgualAZero() {
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.ZERO);
        colaboradorPadraoEntity.setSalarioBase(BigDecimal.ZERO);
        when(colaboradorRepository.save(any(ColaboradorPadrao.class))).thenReturn(colaboradorPadraoEntity);

        ColaboradorDetailResponse response = colaboradorService.criar(colaboradorPadraoRequest);

        assertThat(response.getSalarioBase()).isEqualTo(new BigDecimal("0.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    @DisplayName("CT-020 - Permitir cadastro de Comissionado com zero vendas (Limite inferior de RN004)")
    void devePermitirCadastroDeComissionadoComZeroVendas() {
        colaboradorComissionadoRequest.setValorVendas(BigDecimal.ZERO);
        colaboradorComissionadoEntity.setValorVendas(BigDecimal.ZERO);
        when(colaboradorRepository.save(any(ColaboradorComissionado.class))).thenReturn(colaboradorComissionadoEntity);

        ColaboradorDetailResponse response = colaboradorService.criar(colaboradorComissionadoRequest);

        assertThat(response.getAdicionais()).isEqualTo(new BigDecimal("0.00"));
        assertThat(response.getSalarioFinal()).isEqualTo(new BigDecimal("3000.00"));
    }

    @Test
    @DisplayName("CT-021 - Consultar colaborador específico pela matrícula (RF005/RF009)")
    void deveConsultarColaboradorEspecificoPelaMatricula() {
        List<Colaborador> colaboradores = List.of(colaboradorComissionadoEntity);
        when(colaboradorRepository.findByFiltro(eq("MAT0002"), isNull(), any(Pageable.class)))
            .thenReturn(new PageImpl<>(colaboradores));

        Page<ColaboradorListResponse> pagina = colaboradorService.listar(0, 10, "id", "asc", "MAT0002", null);

        assertThat(pagina.getContent())
            .extracting(ColaboradorListResponse::getMatricula)
            .containsExactly("MAT0002");
    }

    private static ResumoFolhaPagamentoProjection projecao(String tipo, Long quantidade, Double valorTotal) {
        ResumoFolhaPagamentoProjection projecao = mock(ResumoFolhaPagamentoProjection.class);
        when(projecao.getTipo()).thenReturn(tipo);
        when(projecao.getQuantidade()).thenReturn(quantidade);
        when(projecao.getValorTotal()).thenReturn(valorTotal);
        return projecao;
    }
}