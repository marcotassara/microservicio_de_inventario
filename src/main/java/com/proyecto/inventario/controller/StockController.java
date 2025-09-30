package com.proyecto.inventario.controller;

import com.proyecto.inventario.dto.StockConProductoDTO;
import com.proyecto.inventario.model.Stock;
import com.proyecto.inventario.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventario")
public class StockController {

    @Autowired
    private StockService stockService;

    // Endpoint para obtener el stock de un producto
    // GET http://localhost:8084/api/v1/inventario/{productoId}
    @GetMapping("/{productoId}")
    public ResponseEntity<Stock> getStockPorProductoId(@PathVariable Long productoId) {
        Stock stock = stockService.obtenerStockPorProductoId(productoId);
        return ResponseEntity.ok(stock);
    }

       @GetMapping("/{productoId}/detalle")
    public ResponseEntity<StockConProductoDTO> getStockConDetalle(@PathVariable Long productoId) {
        StockConProductoDTO respuestaCombinada = stockService.obtenerStockConDetalleDeProducto(productoId);
        return ResponseEntity.ok(respuestaCombinada);
    }

    // Endpoint para registrar una entrada de stock
    // POST http://localhost:8084/api/v1/inventario/{productoId}/entrada?cantidad=10
    @PostMapping("/{productoId}/entrada")
    public ResponseEntity<Stock> registrarEntrada(@PathVariable Long productoId, @RequestParam int cantidad) {
        Stock stockActualizado = stockService.registrarEntrada(productoId, cantidad);
        return ResponseEntity.ok(stockActualizado);
    }

    // Endpoint para registrar una salida de stock
    // POST http://localhost:8084/api/v1/inventario/{productoId}/salida?cantidad=2
    @PostMapping("/{productoId}/salida")
    public ResponseEntity<Stock> registrarSalida(@PathVariable Long productoId, @RequestParam int cantidad) {
        Stock stockActualizado = stockService.registrarSalida(productoId, cantidad);
        return ResponseEntity.ok(stockActualizado);
    }
}