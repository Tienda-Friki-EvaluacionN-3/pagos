package com.tiendafriki.pagos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendafriki.pagos.dto.PagoRequestDTO;
import com.tiendafriki.pagos.dto.PagoResponseDTO;
import com.tiendafriki.pagos.service.PagoService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService service;

    @Operation(
        summary = "Listar pagos",
        description = "Obtiene una lista con todos los pagos registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de logica de negocio")  ,
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/listar")
    public List<PagoResponseDTO> listar() {

        return service.listar();
    }

    @Operation(
        summary = "Buscar pago por id",
        description = "Obtiene un pago por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de logica de negocio"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/buscarId/{id}")
    public PagoResponseDTO buscarPorId(@PathVariable Integer id) {

        return service.buscarPorId(id);
    }

    @Operation(
        summary = "Buscar pagos por fecha",
        description = "Obtiene una lista de pagos por su fecha de realización"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de logica de negocio"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/buscarPorFecha/{fechaPago}")
    public List<PagoResponseDTO> buscarPorFecha(@PathVariable LocalDate fechaPago) {

        return service.buscarPorFecha(fechaPago);

    }

    @Operation(
        summary = "Buscar pago por id de pedido",
        description = "Obtiene un pago por la id del pedido al que pertenece"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de logica de negocio"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor o en la comunicación entre microservicios")
    })

    @GetMapping("/buscarPorIdPedido/{id}")
    public PagoResponseDTO  buscarPorIdPedido(@PathVariable Integer id) {

        return service.buscarPorIdPedido(id);
    }

    @Operation(
        summary = "Registrar pago",
        description = "Permite registrar los intentos de un pago en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago registrado en el sistema"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de lógica de negocio")  ,
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    
    @PostMapping("/crear")
    public ResponseEntity<String> crear(@Valid @RequestBody PagoRequestDTO pago) {

        String mensaje = service.guardar(pago);

        return ResponseEntity.status(201).body(mensaje);
    }

}
