package com.proyecto.inventario.config;

import com.proyecto.inventario.dto.ProductoDTO;
import com.proyecto.inventario.repository.StockRepository;
import com.proyecto.inventario.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private WebClient webClientProductos; 

    @Override
    public void run(String... args) {
        // Solo ejecutamos si la tabla de stock está vacía
        if (stockRepository.count() == 0) {
            System.out.println("🧪 Tabla de stock vacía, consultando productos existentes...");

            try {
                // 1. Llamamos al microservicio de productos para obtener la lista completa
                ProductoDTO[] productos = webClientProductos.get()
                        .uri("") // El endpoint raíz que devuelve todos los productos
                        .retrieve()
                        .bodyToMono(ProductoDTO[].class)
                        .block(); // Esperamos la respuesta

                // 2. Si obtuvimos productos, creamos stock para cada uno
                if (productos != null && productos.length > 0) {
                    System.out.println("📦 Se encontraron " + productos.length + " productos. Creando stock inicial...");
                    for (ProductoDTO producto : productos) {
                        // Para cada producto, registramos una entrada de stock con una cantidad por defecto (ej. 100)
                        stockService.registrarEntrada(producto.getId(), 100);
                        System.out.println("   -> Stock creado para: " + producto.getNombre());
                    }
                    System.out.println("✅ Datos de stock cargados dinámicamente. Productos en inventario: " + stockRepository.count());
                } else {
                    System.out.println("⚠️ No se encontraron productos para crear stock inicial.");
                }

            } catch (Exception e) {
                System.out.println("❌ Error al comunicarse con el microservicio de productos: " + e.getMessage());
            }

        } else {
            System.out.println("ℹ️ La tabla de stock ya tiene datos, no se requiere carga inicial.");
        }
    }
}