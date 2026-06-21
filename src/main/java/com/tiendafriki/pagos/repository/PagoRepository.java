package com.tiendafriki.pagos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendafriki.pagos.Enum.EstadoPago;
import com.tiendafriki.pagos.model.Pago;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByFechaPago(LocalDate fechaPago);

    Optional<Pago> findByPedidoId(Integer pedidoId);

    Optional<Pago> findByPedidoIdAndEstado(Integer pedidoId,EstadoPago estado);

}
