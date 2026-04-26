package com.formacionbdi.springboot.app.inventarioautos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.inventarioautos.models.Inventario;
import com.formacionbdi.springboot.app.inventarioautos.models.service.InventarioService;

@RestController
public class InventarioController {
	
	@Autowired
	// @Qualifier("serviceRestTemplate")
	@Qualifier("serviceFeign")
	private InventarioService inventarioService;
	
	@GetMapping("/listar")
	public List<Inventario> listar(){
		return inventarioService.findAll();
	}
	
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Inventario detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return inventarioService.findById(id, cantidad);
	}

	@DeleteMapping("/eliminar/{id}")
	public void eliminar(@PathVariable Long id) {
		inventarioService.deleteById(id);
	}

}
