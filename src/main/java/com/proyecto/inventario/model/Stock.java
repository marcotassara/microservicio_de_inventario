package com.proyecto.inventario.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "STOCK")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_seq")
    @SequenceGenerator(name = "stock_seq", sequenceName = "STOCK_SEQ", allocationSize = 1)
    @Column(name = "IDSTOCK")
    private Long id;

    @Column(name = "IDPRODUCTO", unique = true, nullable = false)
    private Long productoId;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;
}