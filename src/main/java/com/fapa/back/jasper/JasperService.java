package com.fapa.back.jasper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fapa.back.shared.exceptions.BadRequestException;
import com.fapa.back.shared.exceptions.NotFoundException;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class JasperService {

    private final Connection connection;

    private Map<String,Object> params = new HashMap<>();

    public static final String FOLHA_PAGAMENTO = "folha_pagamento";
    public static final String RESUMO_RELATORIO = "resumo_relatorio";

    public JasperService(Connection connection) {
        this.connection = connection;
    }

    public void exportToPdf(HttpServletResponse response, String fileName) {
        try {
            byte[] bytes;
            InputStream jasperStream = getClass().getClassLoader().getResourceAsStream("jasper/" + fileName + ".jasper");
            if (jasperStream == null) {
                throw new NotFoundException("Jasper file not found: " + fileName);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, connection);
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            response.getOutputStream().write(bytes);
        }
        catch (IOException | JRException e) {
            throw new BadRequestException(fileName + " Jasper file not found: " + e.getMessage());
        }
    }

}
