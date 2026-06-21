package com.tiendafriki.pagos.dto;

import com.tiendafriki.pagos.Enum.MetodoPago;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagoRequestDTO {

    @NotNull(message = "[+] El id del pedido es obligatorio [X_X]")
    @Positive(message = "[+] El id del pedido debe ser un numero positivo [X_X]")
    private Integer pedidoId;

    @NotNull(message = "[+] El monto es obligatorio [X_X]")
    @PositiveOrZero(message = "[+] El monto no puede ser negativo [X_X]")
    private Double monto;

    @NotNull(message = "[+] El metodo de pago es obligatorio [X_X]")
    private MetodoPago metodoPago;

}
