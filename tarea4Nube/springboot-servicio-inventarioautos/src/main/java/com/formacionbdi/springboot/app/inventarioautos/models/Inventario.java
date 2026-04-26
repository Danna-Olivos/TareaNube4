package com.formacionbdi.springboot.app.inventarioautos.models;

public class Inventario {
	private Auto auto;
	private Integer cantidad;

	public Inventario() {}

	public Inventario(Auto auto, Integer cantidad) {
		this.auto = auto;
		this.cantidad = cantidad;
	}

	public Auto getAuto() { return auto; }
	public void setAuto(Auto auto) { this.auto = auto; }

	public Integer getCantidad() { return cantidad; }
	public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

	public Double getTotal() {
		return auto.getPrecio() * cantidad.doubleValue();
	}
}
