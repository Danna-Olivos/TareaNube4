package com.formacionbdi.springboot.app.inventarioautos.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.app.inventarioautos.clientes.AutoClienteRest;
import com.formacionbdi.springboot.app.inventarioautos.models.Auto;
import com.formacionbdi.springboot.app.inventarioautos.models.Inventario;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@Service("serviceFeign")
public class InventarioServiceFeign implements InventarioService {

	@Autowired
	private AutoClienteRest clienteFeign;

	@Override
	@HystrixCommand(fallbackMethod = "findAllFallback")
	public List<Inventario> findAll() {
		return clienteFeign.listar()
				.stream()
				.map(p -> new Inventario(p, 1))
				.collect(Collectors.toList());
	}

	public List<Inventario> findAllFallback() {
		System.out.println("=== FALLBACK ACTIVADO (Hystrix) findAll ===");

		Auto auto = new Auto();
		auto.setId(-1L);
		auto.setMarca("Fallback Hystrix");
		auto.setModelo("Servicio lento o caído");
		auto.setPrecio(0.0);

		return List.of(new Inventario(auto, 0));
	}

	@Override
	@HystrixCommand(fallbackMethod = "findByIdFallback")
	public Inventario findById(Long id, Integer cantidad) {
		return new Inventario(clienteFeign.detalle(id), cantidad);
	}

	public Inventario findByIdFallback(Long id, Integer cantidad) {
		System.out.println("=== FALLBACK ACTIVADO (Hystrix) findById ===");

		Auto auto = new Auto();
		auto.setId(id);
		auto.setMarca("Servicio no disponible");
		auto.setModelo("Timeout o error");
		auto.setPrecio(0.0);

		return new Inventario(auto, cantidad);
	}

	@Override
	@HystrixCommand(fallbackMethod = "deleteByIdFallback")
	public void deleteById(Long id) {
		clienteFeign.eliminar(id);
	}

	public void deleteByIdFallback(Long id) {
		System.out.println("=== FALLBACK ACTIVADO (Hystrix) deleteById ===");
	}
}