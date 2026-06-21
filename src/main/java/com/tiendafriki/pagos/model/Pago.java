package com.tiendafriki.pagos.model;

import java.time.LocalDate;

import com.tiendafriki.pagos.Enum.EstadoPago;
import com.tiendafriki.pagos.Enum.MetodoPago;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer pedidoId;

    private Double monto;
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

}
