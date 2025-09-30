package com.proyecto.inventario.dto;

import com.proyecto.inventario.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta clase representa la respuesta combinada que daremos
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockConProductoDTO {

    private Stock stock;
    private ProductoDTO producto;
}