package com.formacionbdi.springboot.app.inventarioautos.clientes;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.formacionbdi.springboot.app.inventarioautos.models.Auto;

@FeignClient(name = "servicio-autos", url = "http://localhost:8003", fallback = AutoClienteRestFallback.class)
public interface AutoClienteRest {
    
    @GetMapping("/listar")
    List<Auto> listar();
    
    @GetMapping("/ver/{id}")
    Auto detalle(@PathVariable Long id);
    
    @DeleteMapping("/eliminar/{id}")
    void eliminar(@PathVariable Long id);
}

@Component
class AutoClienteRestFallback implements AutoClienteRest {
    
    @Override
    public List<Auto> listar() {
        System.out.println("=== FALLBACK ACTIVADO: listar() ===");
        Auto autoError = new Auto();
        autoError.setId(-1L);  // ID negativo para identificar error
        autoError.setMarca("Temporalmente fuera de servicio");
        autoError.setModelo("Por favor, intente más tarde");
        autoError.setPrecio(0.0);
        
        return Arrays.asList(autoError);
    }
    
    @Override
    public Auto detalle(@PathVariable Long id) {
        System.out.println("=== FALLBACK ACTIVADO: detalle(" + id + ") ===");
        Auto autoFallback = new Auto();
        autoFallback.setId(id);
        autoFallback.setMarca("Servicio no disponible");
        autoFallback.setModelo("Intente más tarde");
        autoFallback.setPrecio(0.0);
        return autoFallback;
    }
    
    @Override
    public void eliminar(@PathVariable Long id) {
        System.out.println("=== FALLBACK ACTIVADO: eliminar(" + id + ") ===");
    }
}