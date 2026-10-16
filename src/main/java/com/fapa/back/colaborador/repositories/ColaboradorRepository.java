package com.fapa.back.colaborador.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.enums.TipoColaborador;
import com.fapa.back.colaborador.projections.ResumoFolhaPagamentoProjection;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    public boolean existsByMatricula(String matricula);
    @Query("""
        SELECT c
        FROM Colaborador c
        WHERE (
            LOWER(c.nome) LIKE LOWER(CONCAT('%', :nomeMatricula, '%'))
            OR c.matricula LIKE CONCAT('%', :nomeMatricula, '%')
        )
        AND (:tipoColaborador IS NULL OR c.tipoColaborador = :tipoColaborador)
    """)
    Page<Colaborador> findByFiltro(String nomeMatricula, TipoColaborador tipoColaborador, Pageable pageable);

    @Query(nativeQuery = true, value = """
        WITH folha AS (
            SELECT c.id, 'PADRAO' AS tipo, c.salario_base AS salario_final
            FROM colaborador c
            INNER JOIN colaborador_padrao cp ON cp.id = c.id
            UNION ALL
            SELECT c.id, 'COMISSIONADO' AS tipo,
                c.salario_base
                    + (
                        cc.valor_vendas
                        * (cc.porcentagem_comissao / 100)
                    ) AS salario_final
            FROM colaborador c
            INNER JOIN colaborador_comissionado cc ON cc.id = c.id
            UNION ALL
            SELECT c.id, 'PRODUCAO' AS tipo,
                c.salario_base
                    + (cp.quantidade_produzida
                        * cp.valor_unidade_produzida
                    ) AS salario_final
            FROM colaborador c
            INNER JOIN colaborador_producao cp ON cp.id = c.id
        ),
        resultado AS (
            SELECT 'TOTAL' AS tipo, COUNT(*) AS quantidade, SUM(salario_final) AS valor_total, 0 AS ordem
            FROM folha
            UNION ALL
            SELECT
                tipo,
                COUNT(*) AS quantidade,
                SUM(salario_final) AS valor_total,
                CASE
                    WHEN tipo = 'PADRAO' THEN 1
                    WHEN tipo = 'COMISSIONADO' THEN 2
                    WHEN tipo = 'PRODUCAO' THEN 3
                END AS ordem
            FROM folha
            GROUP BY tipo
        )
        SELECT tipo, quantidade, valor_total as valorTotal
        FROM resultado
        ORDER BY ordem
    """)
    List<ResumoFolhaPagamentoProjection> getResumoFolhaPagamento();

}
