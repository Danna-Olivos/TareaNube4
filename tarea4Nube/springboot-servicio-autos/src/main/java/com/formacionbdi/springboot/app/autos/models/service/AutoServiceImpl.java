package com.formacionbdi.springboot.app.autos.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.formacionbdi.springboot.app.autos.models.dao.AutoDao;
import com.formacionbdi.springboot.app.autos.models.entity.Auto;

@Service
public class AutoServiceImpl implements IAutoService {

	@Autowired
	private AutoDao autoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Auto> findAll() {
		return (List<Auto>) autoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Auto findById(Long id) {
		return autoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		autoDao.deleteById(id);
	}
}
