package com.proyecto.inventario.repository;

import com.proyecto.inventario.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    // Método para buscar el stock de un producto específico por su ID
    Optional<Stock> findByProductoId(Long productoId);
}