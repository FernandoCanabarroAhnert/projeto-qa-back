package com.fapa.back.colaborador.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fapa.back.colaborador.controllers.request.ColaboradorComissionadoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorPadraoRequest;
import com.fapa.back.colaborador.controllers.request.ColaboradorProducaoRequest;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:create_tables.sql", "classpath:insert_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ColaboradorControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private ColaboradorPadraoRequest colaboradorPadraoRequest;
    private ColaboradorComissionadoRequest colaboradorComissionadoRequest;
    private ColaboradorProducaoRequest colaboradorProducaoRequest;

    @BeforeEach
    void setup() {
        colaboradorPadraoRequest = new ColaboradorPadraoRequest();
        colaboradorPadraoRequest.setMatricula("MAT0004");
        colaboradorPadraoRequest.setNome("Carlos Silva");
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.valueOf(3500.00));
        colaboradorPadraoRequest.setTipoColaborador("PADRAO");

        colaboradorComissionadoRequest = new ColaboradorComissionadoRequest();
        colaboradorComissionadoRequest.setMatricula("MAT0005");
        colaboradorComissionadoRequest.setNome("Ana Souza");
        colaboradorComissionadoRequest.setSalarioBase(BigDecimal.valueOf(3000.00));
        colaboradorComissionadoRequest.setTipoColaborador("COMISSIONADO");
        colaboradorComissionadoRequest.setValorVendas(BigDecimal.valueOf(15000.00));
        colaboradorComissionadoRequest.setPorcentagemComissao(BigDecimal.valueOf(10.00));
        
        colaboradorProducaoRequest = new ColaboradorProducaoRequest();
        colaboradorProducaoRequest.setMatricula("MAT0006");
        colaboradorProducaoRequest.setNome("Bruno Lima");
        colaboradorProducaoRequest.setSalarioBase(BigDecimal.valueOf(2500.00));
        colaboradorProducaoRequest.setTipoColaborador("PRODUCAO");
        colaboradorProducaoRequest.setQuantidadeProduzida(100);
        colaboradorProducaoRequest.setValorUnidadeProduzida(BigDecimal.valueOf(20.00));    
    }

    @Test 
    @DisplayName("CT-001 - Cadastrar Colaborador Padrão com dados válidos")
    @Order(1)
    void deveCadastrarColaboradorPadraoComDadosValidos() throws Exception {
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorPadraoRequest)))
            .andExpect(status().isCreated());
    }

    @Test 
    @DisplayName("CT-002 -Cadastrar Colaborador Comissionado com dados válidos")
    @Order(2)
    void deveCadastrarColaboradorComissionadoComDadosValidos() throws Exception {
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorComissionadoRequest)))
            .andExpect(status().isCreated());
    }

    @Test 
    @DisplayName("CT-003 - Cadastrar Colaborador por Produção com dados válidos")
    @Order(3)
    void deveCadastrarColaboradorProducaoComDadosValidos() throws Exception {
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorProducaoRequest)))
            .andExpect(status().isCreated());
    }

    @Test 
    @DisplayName("CT-004 - Impedir cadastro com matrícula duplicada (RN001)")
    @Order(4)
    void deveImpedirCadastroComMatriculaDuplicada() throws Exception {
        colaboradorPadraoRequest.setMatricula("MAT0001");
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorPadraoRequest)))
            .andExpect(status().isConflict());
    }

    @Test 
    @DisplayName("CT-005 - Impedir cadastro com nome em branco (RN002)")
    void deveImpedirCadastroComNomeEmBranco() throws Exception {
        colaboradorPadraoRequest.setNome("");
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorPadraoRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    @DisplayName("CT-006 - Impedir cadastro com salário base negativo (RN003)")
    void deveImpedirCadastroComSalarioBaseNegativo() throws Exception {
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.valueOf(-3000.00));
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorPadraoRequest)))
            .andExpect(status().isBadRequest());
        
    }

    @Test 
    @DisplayName("CT-007 - Impedir cadastro com valor de vendas negativo (RN004)")
    void deveImpedirCadastroComValorVendasNegativo() throws Exception {
        colaboradorComissionadoRequest.setValorVendas(BigDecimal.valueOf(-1000.00));
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorComissionadoRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    @DisplayName("CT-008 - Impedir cadastro com percentual de comissão negativo (RN005)")
    void deveImpedirCadastroComPercentualComissaoNegativo() throws Exception {
        colaboradorComissionadoRequest.setPorcentagemComissao(BigDecimal.valueOf(-10.00));
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorComissionadoRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    @DisplayName("CT-009 - Impedir cadastro com quantidade produzida negativa (RN006)")
    void deveImpedirCadastroComQuantidadeProduzidaNegativa() throws Exception {
        colaboradorProducaoRequest.setQuantidadeProduzida(-100);
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorProducaoRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    @DisplayName("CT-010 - Impedir cadastro com valor por unidade negativo (RN007)")
    void deveImpedirCadastroComValorPorUnidadeNegativo() throws Exception {
        colaboradorProducaoRequest.setValorUnidadeProduzida(BigDecimal.valueOf(-5.00));
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorProducaoRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    @DisplayName("CT-011 - Consultar lista de colaboradores cadastrados (RF005)")
    @Order(5)
    void deveConsultarListaDeColaboradoresCadastrados() throws Exception {
        mockMvc.perform(get("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].matricula").value("MAT0001"))
            .andExpect(jsonPath("$.content[0].nome").value("Ana Beatriz Souza"))
            .andExpect(jsonPath("$.content[0].salarioBase").value(3200.00))
            .andExpect(jsonPath("$.content[0].adicionais").value(0.00))
            .andExpect(jsonPath("$.content[0].salarioFinal").value(3200.00))
            .andExpect(jsonPath("$.content[0].tipoColaborador").value("PADRAO"))
            .andExpect(jsonPath("$.numberOfElements").value(6));
    }

    @Test 
    @DisplayName("CT-012 - Calcular salário correto para Colaborador Padrão (RF006)")
    @Order(6)
    void deveCalcularSalarioCorretoParaColaboradorPadrao() throws Exception {
        mockMvc.perform(get("/api/v1/colaboradores/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.matricula").value("MAT0001"))
            .andExpect(jsonPath("$.nome").value("Ana Beatriz Souza"))
            .andExpect(jsonPath("$.salarioBase").value(3200.00))
            .andExpect(jsonPath("$.adicionais").value(0.00))
            .andExpect(jsonPath("$.salarioFinal").value(3200.00))
            .andExpect(jsonPath("$.tipoColaborador").value("PADRAO"));
    }

    @Test 
    @DisplayName("CT-013 - Calcular salário correto para Colaborador Comissionado (RF006)")
    @Order(7)
    void deveCalcularSalarioCorretoParaColaboradorComissionado() throws Exception {
        /*
        * Comissão = Valor das Vendas × Percentual de Comissão
        * Salário Final = Salário Base + Comissão
        */
        mockMvc.perform(get("/api/v1/colaboradores/{id}", 2L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.matricula").value("MAT0002"))
            .andExpect(jsonPath("$.nome").value("Eduardo Monteiro"))
            .andExpect(jsonPath("$.salarioBase").value(2500.00))
            .andExpect(jsonPath("$.adicionais").value(2250.00))
            .andExpect(jsonPath("$.salarioFinal").value(4750.00))
            .andExpect(jsonPath("$.tipoColaborador").value("COMISSIONADO"));
    }

    @Test 
    @DisplayName("CT-014 - Calcular salário correto para Colaborador por Produção (RF006)")
    @Order(8)
    void deveCalcularSalarioCorretoParaColaboradorPorProducao() throws Exception {
        /*
        * A produtividade deverá ser calculada pela fórmula:
        * Produtividade = Quantidade Produzida × Valor por Unidade Produzida
        * Salário Final = Salário Base + Produtividade
        */
        mockMvc.perform(get("/api/v1/colaboradores/{id}", 3L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.matricula").value("MAT0003"))
            .andExpect(jsonPath("$.nome").value("José Antônio Ferraz"))
            .andExpect(jsonPath("$.salarioBase").value(1800.00))
            .andExpect(jsonPath("$.adicionais").value(2975.00))
            .andExpect(jsonPath("$.salarioFinal").value(4775.00))
            .andExpect(jsonPath("$.tipoColaborador").value("PRODUCAO"));
    }

    @Test 
    @DisplayName("CT-015 - Gerar Folha de Pagamento detalhada (RF007)")
    @Order(9)
    void deveGerarFolhaDePagamentoDetalhada() throws Exception {
        mockMvc.perform(get("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].matricula").value("MAT0001"))
            .andExpect(jsonPath("$.content[0].nome").value("Ana Beatriz Souza"))
            .andExpect(jsonPath("$.content[0].salarioBase").value(3200.00))
            .andExpect(jsonPath("$.content[0].adicionais").value(0.00))
            .andExpect(jsonPath("$.content[0].salarioFinal").value(3200.00))
            .andExpect(jsonPath("$.content[0].tipoColaborador").value("PADRAO"))
            .andExpect(jsonPath("$.numberOfElements").value(6));
    }

    @Test 
    @DisplayName("CT-016 - Emitir Resumo da Folha com somatório correto (RF008)")
    @Order(10)
    void deveEmitirResumoDaFolhaComSomatorioCorreto() throws Exception {
        mockMvc.perform(get("/api/v1/colaboradores/resumo/json")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].tipo").value("TOTAL"))
            .andExpect(jsonPath("$[0].quantidade").value(6))
            .andExpect(jsonPath("$[0].valorTotal").value(25225.00))
            .andExpect(jsonPath("$[1].tipo").value("PADRAO"))
            .andExpect(jsonPath("$[1].quantidade").value(2))
            .andExpect(jsonPath("$[1].valorTotal").value(6700.00))
            .andExpect(jsonPath("$[2].tipo").value("COMISSIONADO"))
            .andExpect(jsonPath("$[2].quantidade").value(2))
            .andExpect(jsonPath("$[2].valorTotal").value(9250.00))
            .andExpect(jsonPath("$[3].tipo").value("PRODUCAO"))
            .andExpect(jsonPath("$[3].quantidade").value(2))
            .andExpect(jsonPath("$[3].valorTotal").value(9275.00));
    }

    @Test 
    @DisplayName("CT-017 - Alterar salário base de um colaborador existente (RF009)")
    @Order(11)
    void deveAlterarSalarioBaseDeUmColaboradorExistente() throws Exception {
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.valueOf(4000.00));    
        mockMvc.perform(put("/api/v1/colaboradores/{id}", 4L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorPadraoRequest)))
            .andExpect(status().isOk());
    }

    @Test 
    @DisplayName("CT-018 - Excluir um colaborador do sistema (RF009)")
    @Order(12)
    void deveExcluirUmColaboradorDoSistema() throws Exception {
        mockMvc.perform(delete("/api/v1/colaboradores/{id}", 1L))
            .andExpect(status().isNoContent());
    }

    @Test 
    @DisplayName("CT-019 - Permitir cadastro com Salário Base igual a zero (Limite inferior de RN003)")
    @Order(13)
    void devePermitirCadastroComSalarioBaseIgualAZero() throws Exception {
        colaboradorPadraoRequest = new ColaboradorPadraoRequest();
        colaboradorPadraoRequest.setMatricula("MAT0007");
        colaboradorPadraoRequest.setNome("Carlos Silva");
        colaboradorPadraoRequest.setSalarioBase(BigDecimal.valueOf(0.00));
        colaboradorPadraoRequest.setTipoColaborador("PADRAO");
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorPadraoRequest)))
            .andExpect(status().isCreated());
    }

    @Test 
    @DisplayName("CT-020 - Permitir cadastro de Comissionado com zero vendas (Limite inferior de RN004)")
    @Order(14)
    void devePermitirCadastroDeComissionadoComZeroVendas() throws Exception {
        colaboradorComissionadoRequest = new ColaboradorComissionadoRequest();
        colaboradorComissionadoRequest.setMatricula("MAT0008");
        colaboradorComissionadoRequest.setNome("Ana Souza");
        colaboradorComissionadoRequest.setSalarioBase(BigDecimal.valueOf(3000.00));
        colaboradorComissionadoRequest.setTipoColaborador("COMISSIONADO");
        colaboradorComissionadoRequest.setValorVendas(BigDecimal.valueOf(0.00));
        colaboradorComissionadoRequest.setPorcentagemComissao(BigDecimal.valueOf(10.00));
        mockMvc.perform(post("/api/v1/colaboradores")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(colaboradorComissionadoRequest)))
            .andExpect(status().isCreated());
    }

    @Test 
    @Order(15)
    @DisplayName("CT-021 - Consultar colaborador específico pela matrícula (RF005/RF009)")
    void deveConsultarColaboradorEspecificoPelaMatricula() throws Exception {
        mockMvc.perform(get("/api/v1/colaboradores?nomeMatricula={matricula}", "MAT0002")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(2))
            .andExpect(jsonPath("$.content[0].matricula").value("MAT0002"))
            .andExpect(jsonPath("$.content[0].nome").value("Eduardo Monteiro"))
            .andExpect(jsonPath("$.content[0].salarioBase").value(2500.00))
            .andExpect(jsonPath("$.content[0].adicionais").value(2250.00))
            .andExpect(jsonPath("$.content[0].salarioFinal").value(4750.00))
            .andExpect(jsonPath("$.content[0].tipoColaborador").value("COMISSIONADO"));
    }

}
