package com.formacionbdi.springboot.app.inventarioautos.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.inventarioautos.models.Inventario;

public interface InventarioService {

	public List<Inventario> findAll();

	public Inventario findById(Long id, Integer cantidad);

	public void deleteById(Long id);
}

