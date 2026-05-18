package com.formacionbdi.springboot.app.autos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formacionbdi.springboot.app.autos.controllers.AutoController;
import com.formacionbdi.springboot.app.autos.models.entity.Auto;
import com.formacionbdi.springboot.app.autos.models.service.IAutoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas JUnit 5 + MockMvc para AutoController.
 *
 * Cubre los cuatro métodos CRUD:
 *   CREATE  → POST  /autos/crear
 *   READ    → GET   /autos/listar  y  GET /autos/ver/{id}
 *   UPDATE  → PUT   /autos/editar/{id}
 *   DELETE  → DELETE /autos/eliminar/{id}
 *
 * Se utiliza @WebMvcTest para levantar solo la capa web y
 * @MockBean para simular IAutoService sin base de datos.
 */
@WebMvcTest(AutoController.class)
class AutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAutoService autoService;

    private ObjectMapper objectMapper;
    private Auto auto1;
    private Auto auto2;

    // ──────────────────────────────────────────
    // Datos de prueba reutilizables
    // ──────────────────────────────────────────
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        auto1 = new Auto();
        auto1.setId(1L);
        auto1.setMarca("Toyota");
        auto1.setModelo("Corolla");
        auto1.setAnio(2023);
        auto1.setPrecio(350000.00);
        auto1.setCreateAt(new Date());

        auto2 = new Auto();
        auto2.setId(2L);
        auto2.setMarca("Honda");
        auto2.setModelo("Civic");
        auto2.setAnio(2022);
        auto2.setPrecio(320000.00);
        auto2.setCreateAt(new Date());
    }

    // ══════════════════════════════════════════
    // READ – listar todos
    // ══════════════════════════════════════════

    @Test
    @DisplayName("GET /autos/listar → 200 OK con lista de autos")
    void listar_debeRetornarListaDeAutos() throws Exception {
        List<Auto> autos = Arrays.asList(auto1, auto2);
        when(autoService.findAll()).thenReturn(autos);

        mockMvc.perform(get("/autos/listar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.autos", hasSize(2)))
                .andExpect(jsonPath("$.autos[0].marca", is("Toyota")))
                .andExpect(jsonPath("$.autos[1].marca", is("Honda")))
                .andExpect(jsonPath("$.total", is(2)));

        verify(autoService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /autos/listar → 200 OK con lista vacía")
    void listar_debeRetornarListaVacia() throws Exception {
        when(autoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/autos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.autos", hasSize(0)))
                .andExpect(jsonPath("$.total", is(0)));
    }

    // ══════════════════════════════════════════
    // READ – ver por id
    // ══════════════════════════════════════════

    @Test
    @DisplayName("GET /autos/ver/{id} → 200 OK cuando el auto existe")
    void detalle_debeRetornarAutoExistente() throws Exception {
        when(autoService.findById(1L)).thenReturn(auto1);

        mockMvc.perform(get("/autos/ver/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.marca", is("Toyota")))
                .andExpect(jsonPath("$.modelo", is("Corolla")))
                .andExpect(jsonPath("$.anio", is(2023)));

        verify(autoService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /autos/ver/{id} → 404 cuando el auto no existe")
    void detalle_debeRetornar404_cuandoNoExiste() throws Exception {
        when(autoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/autos/ver/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString("99")));
    }

    // ══════════════════════════════════════════
    // CREATE – guardar nuevo auto
    // ══════════════════════════════════════════

    @Test
    @DisplayName("POST /autos/crear → 201 Created con auto guardado")
    void crear_debeGuardarAutoYRetornar201() throws Exception {
        Auto nuevoAuto = new Auto();
        nuevoAuto.setMarca("Nissan");
        nuevoAuto.setModelo("Sentra");
        nuevoAuto.setAnio(2024);
        nuevoAuto.setPrecio(290000.00);

        Auto autoGuardado = new Auto();
        autoGuardado.setId(3L);
        autoGuardado.setMarca("Nissan");
        autoGuardado.setModelo("Sentra");
        autoGuardado.setAnio(2024);
        autoGuardado.setPrecio(290000.00);

        when(autoService.save(any(Auto.class))).thenReturn(autoGuardado);

        mockMvc.perform(post("/autos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoAuto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.marca", is("Nissan")))
                .andExpect(jsonPath("$.modelo", is("Sentra")));

        verify(autoService, times(1)).save(any(Auto.class));
    }

    @Test
    @DisplayName("POST /autos/crear → 400 Bad Request cuando falta la marca")
    void crear_debeRetornar400_cuandoFaltaMarca() throws Exception {
        Auto autoSinMarca = new Auto();
        autoSinMarca.setModelo("Modelo X");
        autoSinMarca.setAnio(2024);
        autoSinMarca.setPrecio(500000.00);

        mockMvc.perform(post("/autos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autoSinMarca)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", notNullValue()));

        verify(autoService, never()).save(any(Auto.class));
    }

    // ══════════════════════════════════════════
    // UPDATE – editar auto existente
    // ══════════════════════════════════════════

    @Test
    @DisplayName("PUT /autos/editar/{id} → 200 OK con auto actualizado")
    void editar_debeActualizarAutoExistente() throws Exception {
        Auto datosActualizados = new Auto();
        datosActualizados.setMarca("Toyota");
        datosActualizados.setModelo("Camry");
        datosActualizados.setAnio(2024);
        datosActualizados.setPrecio(420000.00);

        Auto autoEditado = new Auto();
        autoEditado.setId(1L);
        autoEditado.setMarca("Toyota");
        autoEditado.setModelo("Camry");
        autoEditado.setAnio(2024);
        autoEditado.setPrecio(420000.00);

        when(autoService.findById(1L)).thenReturn(auto1);
        when(autoService.save(any(Auto.class))).thenReturn(autoEditado);

        mockMvc.perform(put("/autos/editar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosActualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.modelo", is("Camry")))
                .andExpect(jsonPath("$.precio", is(420000.00)));

        verify(autoService, times(1)).findById(1L);
        verify(autoService, times(1)).save(any(Auto.class));
    }

    @Test
    @DisplayName("PUT /autos/editar/{id} → 404 cuando el auto no existe")
    void editar_debeRetornar404_cuandoNoExiste() throws Exception {
        when(autoService.findById(99L)).thenReturn(null);

        Auto datos = new Auto();
        datos.setMarca("Ford");
        datos.setModelo("Focus");

        mockMvc.perform(put("/autos/editar/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString("99")));

        verify(autoService, never()).save(any(Auto.class));
    }

    // ══════════════════════════════════════════
    // DELETE – eliminar auto
    // ══════════════════════════════════════════

    @Test
    @DisplayName("DELETE /autos/eliminar/{id} → 200 OK cuando el auto existe")
    void eliminar_debeEliminarAutoExistente() throws Exception {
        when(autoService.findById(1L)).thenReturn(auto1);
        doNothing().when(autoService).deleteById(1L);

        mockMvc.perform(delete("/autos/eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", containsString("1")));

        verify(autoService, times(1)).findById(1L);
        verify(autoService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /autos/eliminar/{id} → 404 cuando el auto no existe")
    void eliminar_debeRetornar404_cuandoNoExiste() throws Exception {
        when(autoService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/autos/eliminar/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString("99")));

        verify(autoService, never()).deleteById(anyLong());
    }
}
