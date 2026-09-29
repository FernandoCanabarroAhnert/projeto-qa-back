package com.fapa.back.colaborador.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fapa.back.colaborador.controllers.request.ColaboradorRequest;
import com.fapa.back.colaborador.controllers.response.ColaboradorDetailResponse;
import com.fapa.back.colaborador.controllers.response.ColaboradorListResponse;
import com.fapa.back.colaborador.services.ColaboradorService;
import com.fapa.back.jasper.JasperService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/v1/colaboradores")
@RequiredArgsConstructor 
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final JasperService jasperService;

    @GetMapping 
    public ResponseEntity<Page<ColaboradorListResponse>> listar(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "") String nomeMatricula,
        @RequestParam(required = false) String tipoColaborador
    ) {
        Page<ColaboradorListResponse> colaboradores = colaboradorService.listar(page, size, sort, direction, nomeMatricula, tipoColaborador);
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorDetailResponse> obterPorId(@PathVariable Long id) {
        ColaboradorDetailResponse colaborador = colaboradorService.obterPorId(id);
        return ResponseEntity.ok(colaborador);
    }

    @PostMapping
    public ResponseEntity<ColaboradorDetailResponse> criar(@RequestBody @Valid ColaboradorRequest colaborador) {
        ColaboradorDetailResponse novoColaborador = colaboradorService.criar(colaborador);
        return ResponseEntity.ok(novoColaborador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorDetailResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ColaboradorRequest colaborador) {
        ColaboradorDetailResponse colaboradorAtualizado = colaboradorService.atualizar(id, colaborador);
        return ResponseEntity.ok(colaboradorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        colaboradorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/folha/pdf")
    public void gerarFolhaPdf(HttpServletResponse response) {
        setResponseHeaders(response, "folha_pagamento");
        jasperService.exportToPdf(response, JasperService.FOLHA_PAGAMENTO);
    }

    @GetMapping("/resumo/pdf")
    public void gerarResumoPdf(HttpServletResponse response) {
        setResponseHeaders(response, "resumo_relatorio");
        jasperService.exportToPdf(response, JasperService.RESUMO_RELATORIO);
    }

    private void setResponseHeaders(HttpServletResponse response, String nomeRelatorio) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));
        String fileName = nomeRelatorio + "_" + currentDateTime + ".pdf";
        String headerValue = "inline; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

}
