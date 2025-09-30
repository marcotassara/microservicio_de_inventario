package com.proyecto.inventario.service;

import com.proyecto.inventario.dto.ProductoDTO;
import com.proyecto.inventario.dto.StockConProductoDTO;
import com.proyecto.inventario.model.Stock;
import com.proyecto.inventario.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WebClient webClientProductos;

    private static final int STOCK_MINIMO = 5; // Umbral para alertas   

    // Consulta el stock de un producto
    @Transactional(readOnly = true)
    public Stock obtenerStockPorProductoId(Long productoId) {
        return stockRepository.findByProductoId(productoId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró stock para el producto ID: " + productoId));
    }

     // 👇 NUEVO MÉTODO PARA COMBINAR LA INFORMACIÓN
    public StockConProductoDTO obtenerStockConDetalleDeProducto(Long productoId) {
        // 1. Obtenemos el stock de nuestra propia base de datos
        Stock stock = obtenerStockPorProductoId(productoId);

        // 2. Usamos WebClient para llamar al microservicio de productos
        System.out.println("📞 Llamando al microservicio de productos para el ID: " + productoId);
        ProductoDTO producto = webClientProductos.get()
                .uri("/{id}", productoId) // Le pasamos el ID del producto
                .retrieve() // Realizamos la llamada
                .bodyToMono(ProductoDTO.class) // Convertimos la respuesta a nuestro DTO
                .block(); // Esperamos la respuesta (en modo síncrono para este ejemplo)

        // 3. Combinamos ambas informaciones y la retornamos
        return new StockConProductoDTO(stock, producto);
    }

    // Registra una entrada de stock (ej. una nueva compra)
    @Transactional
    public Stock registrarEntrada(Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }

        Stock stock = stockRepository.findByProductoId(productoId)
                .orElse(new Stock()); // Si no existe, crea un nuevo registro
        
        if (stock.getId() == null) {
            stock.setProductoId(productoId);
            stock.setCantidad(0);
        }

        stock.setCantidad(stock.getCantidad() + cantidad);
        return stockRepository.save(stock);
    }

    // Registra una salida de stock (ej. una venta)
    @Transactional
    public Stock registrarSalida(Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }

        Stock stock = obtenerStockPorProductoId(productoId);

        if (stock.getCantidad() < cantidad) {
            throw new IllegalStateException("Stock insuficiente. Cantidad disponible: " + stock.getCantidad());
        }

        stock.setCantidad(stock.getCantidad() - cantidad);

        // Opcional: Lógica para alerta de stock bajo
        if (stock.getCantidad() <= STOCK_MINIMO) {
            System.out.println("⚠️ ALERTA: Stock bajo para el producto ID " + productoId + ". Cantidad restante: " + stock.getCantidad());
        }

        return stockRepository.save(stock);
    }
}