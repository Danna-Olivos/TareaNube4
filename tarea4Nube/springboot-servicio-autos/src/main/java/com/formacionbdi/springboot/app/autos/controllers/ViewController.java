package com.formacionbdi.springboot.app.autos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "forward:/views/lista_autos_view.html";
    }
    
    @GetMapping("/autos/vista")
    public String vistaAutos() {
        return "forward:/views/lista_autos_view.html";
    }
}
