package model;

import java.io.Serializable;

public enum EstadoPedido implements Serializable {
    PENDIENTE,
    PAGADO,
    ENVIADO,
    ENTREGADO,
    CANCELADO
}
