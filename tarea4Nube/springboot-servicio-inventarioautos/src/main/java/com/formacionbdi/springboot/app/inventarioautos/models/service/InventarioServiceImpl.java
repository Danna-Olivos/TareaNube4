package com.formacionbdi.springboot.app.inventarioautos.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacionbdi.springboot.app.inventarioautos.models.Auto;
import com.formacionbdi.springboot.app.inventarioautos.models.Inventario;

@Service("serviceRestTemplate")
public class InventarioServiceImpl implements InventarioService {

	@Autowired
	private RestTemplate clienteRest;

	@Override
	public List<Inventario> findAll() {
		List<Auto> autos = Arrays.asList(
				clienteRest.getForObject("http://localhost:8003/listar", Auto[].class)
		);
		return autos.stream().map(a -> new Inventario(a, 1)).collect(Collectors.toList());
	}

	@Override
	public Inventario findById(Long id, Integer cantidad) {
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("id", id.toString());
		Auto auto = clienteRest.getForObject("http://localhost:8003/ver/{id}", Auto.class, pathVariables);
		return new Inventario(auto, cantidad);
	}

	@Override
	public void deleteById(Long id) {
		clienteRest.delete("http://localhost:8003/eliminar/{id}", id);
	}

}
