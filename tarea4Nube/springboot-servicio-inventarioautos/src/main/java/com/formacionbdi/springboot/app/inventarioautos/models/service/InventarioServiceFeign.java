package com.formacionbdi.springboot.app.inventarioautos.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.springboot.app.inventarioautos.clientes.AutoClienteRest;
import com.formacionbdi.springboot.app.inventarioautos.models.Inventario;

@Service("serviceFeign")
public class InventarioServiceFeign implements InventarioService {

	@Autowired
	private AutoClienteRest clienteFeign;

	@Override
	public List<Inventario> findAll() {
		return clienteFeign.listar().stream().map(p -> new Inventario(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Inventario findById(Long id, Integer cantidad) {
		return new Inventario(clienteFeign.detalle(id), cantidad);
	}
	@Override
	public void deleteById(Long id) {
		clienteFeign.eliminar(id);
	}
}
