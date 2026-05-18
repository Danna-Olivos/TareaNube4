package com.formacionbdi.springboot.app.autos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.formacionbdi.springboot.app.autos.models.entity.Auto;
import com.formacionbdi.springboot.app.autos.models.service.IAutoService;

@RestController
@RequestMapping("/autos")
public class AutoController {

    @Autowired
    private IAutoService autoService;

    @Value("${server.port}")
    private int puerto;

    // ──────────────────────────────────────────
    // READ – listar todos
    // GET /autos/listar
    // ──────────────────────────────────────────
    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listar() {
        List<Auto> autos = autoService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("autos", autos);
        response.put("total", autos.size());
        response.put("puerto_servidor", puerto);
        return ResponseEntity.ok(response);
    }

    // ──────────────────────────────────────────
    // READ – obtener por id
    // GET /autos/ver/{id}
    // ──────────────────────────────────────────
    @GetMapping("/ver/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Auto auto = autoService.findById(id);
        if (auto == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "El auto con ID " + id + " no existe en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(auto);
    }

    // ──────────────────────────────────────────
    // CREATE – guardar nuevo auto
    // POST /autos/crear
    // Body: JSON con los campos de Auto
    // ──────────────────────────────────────────
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Auto auto) {
        if (auto.getMarca() == null || auto.getMarca().isBlank()) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "La marca del auto es obligatoria.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        Auto autoGuardado = autoService.save(auto);
        return ResponseEntity.status(HttpStatus.CREATED).body(autoGuardado);
    }

    // ──────────────────────────────────────────
    // UPDATE – actualizar auto existente
    // PUT /autos/editar/{id}
    // Body: JSON con los campos a actualizar
    // ──────────────────────────────────────────
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Auto autoActualizado) {
        Auto autoExistente = autoService.findById(id);
        if (autoExistente == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "No se puede actualizar. El auto con ID " + id + " no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        autoExistente.setMarca(autoActualizado.getMarca());
        autoExistente.setModelo(autoActualizado.getModelo());
        autoExistente.setAnio(autoActualizado.getAnio());
        autoExistente.setPrecio(autoActualizado.getPrecio());
        autoExistente.setCreateAt(autoActualizado.getCreateAt());

        Auto autoEditado = autoService.save(autoExistente);
        return ResponseEntity.ok(autoEditado);
    }

    // ──────────────────────────────────────────
    // DELETE – eliminar por id
    // DELETE /autos/eliminar/{id}
    // ──────────────────────────────────────────
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Auto auto = autoService.findById(id);
        if (auto == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "No se puede eliminar. El auto con ID " + id + " no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        autoService.deleteById(id);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Auto con ID " + id + " eliminado correctamente.");
        return ResponseEntity.ok(respuesta);
    }
}
