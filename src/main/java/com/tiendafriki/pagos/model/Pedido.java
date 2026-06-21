package com.tiendafriki.pagos.model;

import lombok.Data;

@Data
public class Pedido {

    private Integer IdPedido;

    private double total;

    private String estadoPedido;

}
