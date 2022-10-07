package es.udc.ws.app.model.reserva;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlReservaDao {

    Reserva create(Connection connection, Reserva reserva);

    Reserva find(Connection connection, Long idReserva) throws InstanceNotFoundException;

    List<Reserva> findReservas(Connection connection, String email);

    void update(Connection connection, Reserva reserva) throws InstanceNotFoundException;

    void remove(Connection connection, Long idReserva) throws InstanceNotFoundException;

}