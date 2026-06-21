package com.tiendafriki.pagos.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.tiendafriki.pagos.Enum.EstadoPago;
import com.tiendafriki.pagos.Enum.MetodoPago;

@Data
@AllArgsConstructor
public class PagoResponseDTO {

    private Integer id;

    private Integer pedidoId;

    private Double monto;

    private MetodoPago metodoPago;

    private LocalDate fechaPago;

    private EstadoPago estado;

}
