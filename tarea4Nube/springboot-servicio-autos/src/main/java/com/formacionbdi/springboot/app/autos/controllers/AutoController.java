package com.formacionbdi.springboot.app.autos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.autos.models.entity.Auto;
import com.formacionbdi.springboot.app.autos.models.service.IAutoService;

@RestController
public class AutoController {

	@Autowired	
	private IAutoService autoService;

	// @GetMapping("/listar")
	// public List<Auto> listar() {
	// 	return autoService.findAll();
	// }

	@Value("${server.port}")
    private int puerto;

    @GetMapping("/listar")
    public Map<String, Object> listar() {
        System.out.println("Sirviendo petición desde el puerto: " + puerto);
        List<Auto> autos = autoService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("productos", autos);
        response.put("puerto_servidor", puerto);
        return response;
    }

	// @GetMapping("/listar")
	// public List<Auto> listar() throws InterruptedException {
	// 	Thread.sleep(2000); // 2 segundos
	// 	return autoService.findAll();
	// }

	@GetMapping("/ver/{id}")
	public Auto detalle(@PathVariable Long id) {
		return autoService.findById(id);
	}

	@DeleteMapping("/eliminar/{id}")
	public void eliminar(@PathVariable Long id) {
		autoService.deleteById(id);
	}
}
