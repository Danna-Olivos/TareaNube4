package com.formacionbdi.springboot.app.autos.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.autos.models.entity.Auto;

public interface IAutoService {
	public List<Auto> findAll();
	public Auto findById(Long id);
	public void deleteById(Long id);
}
