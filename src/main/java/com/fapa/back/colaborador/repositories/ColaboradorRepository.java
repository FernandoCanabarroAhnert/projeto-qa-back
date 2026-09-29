package com.fapa.back.colaborador.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fapa.back.colaborador.domain.Colaborador;
import com.fapa.back.colaborador.enums.TipoColaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    public boolean existsByMatricula(String matricula);
    @Query("""
        SELECT c
        FROM Colaborador c
        WHERE (
            LOWER(c.nome) LIKE LOWER(CONCAT('%', :nomeMatricula, '%'))
            OR c.matricula LIKE CONCAT('%', :nomeMatricula, '%')
        )
        AND (
            :tipoColaborador IS NULL
            OR c.tipoColaborador = :tipoColaborador
        )
    """)
    Page<Colaborador> findByFiltro(String nomeMatricula, TipoColaborador tipoColaborador, Pageable pageable);

}
