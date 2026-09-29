package com.fapa.back.colaborador.domain;

import java.math.BigDecimal;

import com.fapa.back.colaborador.enums.TipoColaborador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.InheritanceType;

@Entity 
@Table(name = "colaborador")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@EqualsAndHashCode(of = "id")
public abstract class Colaborador {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String matricula;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private BigDecimal salarioBase;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoColaborador tipoColaborador;

    public abstract BigDecimal calcularAdicionais();
    public abstract BigDecimal calcularSalarioFinal();

}
