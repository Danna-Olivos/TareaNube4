package com.formacionbdi.springboot.app.autos.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.autos.models.entity.Auto;

public interface IAutoService {
    List<Auto> findAll();
    Auto findById(Long id);
    Auto save(Auto auto);       // CREATE + UPDATE
    void deleteById(Long id);   // DELETE
}
