package com.formacionbdi.springboot.app.autos.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.formacionbdi.springboot.app.autos.models.entity.Auto;

public interface AutoDao extends CrudRepository<Auto, Long> {
}
