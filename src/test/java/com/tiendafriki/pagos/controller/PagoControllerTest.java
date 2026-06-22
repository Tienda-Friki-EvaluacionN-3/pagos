package com.tiendafriki.pagos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tiendafriki.pagos.Enum.EstadoPago;
import com.tiendafriki.pagos.Enum.MetodoPago;

import com.tiendafriki.pagos.dto.PagoRequestDTO;
import com.tiendafriki.pagos.dto.PagoResponseDTO;
import com.tiendafriki.pagos.service.PagoService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private PagoRequestDTO crearRequestDTO() {

        return new PagoRequestDTO(
                1,
                10000.0,
                MetodoPago.DEBITO
        );
    }

    private PagoResponseDTO crearResponseDTO() {

        return new PagoResponseDTO(
                1,
                1,
                10000.0,
                MetodoPago.DEBITO,
                LocalDate.now(),
                EstadoPago.COMPLETADO
        );
    }

    @Test
    void listarPagos() throws Exception {

        when(service.listar())
                .thenReturn(List.of(crearResponseDTO()));

        mockMvc.perform(get("/pagos/listar"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorId() throws Exception {

        when(service.buscarPorId(1))
                .thenReturn(crearResponseDTO());

        mockMvc.perform(get("/pagos/buscarId/1"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorFecha() throws Exception {

        LocalDate fecha = LocalDate.now();

        when(service.buscarPorFecha(fecha))
                .thenReturn(List.of(crearResponseDTO()));

        mockMvc.perform(
                get("/pagos/buscarPorFecha/" + fecha)
        )
        .andExpect(status().isOk());
    }

    @Test
    void buscarPorIdPedido() throws Exception {

        when(service.buscarPorIdPedido(1))
                .thenReturn(crearResponseDTO());

        mockMvc.perform(get("/pagos/buscarPorIdPedido/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearPago() throws Exception {

        when(service.guardar(any()))
                .thenReturn("[+] Pago registrado correctamente ...");

        mockMvc.perform(post("/pagos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearRequestDTO())))
                .andExpect(status().isCreated());
    }

}