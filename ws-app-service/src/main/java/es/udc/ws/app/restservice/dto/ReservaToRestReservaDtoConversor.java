package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.reserva.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaToRestReservaDtoConversor {

    public static RestReservaDto toRestReservaDto(Reserva reserva) {
        return new RestReservaDto(reserva.getIdReserva(), reserva.getRegisterDateTime(),
                reserva.getUserEmail(), reserva.getNumPlaces(), reserva.getNumCreditCard().substring(reserva.getNumCreditCard().length()-4),
                reserva.getIdExcursion(), reserva.getCancelDateTime(), reserva.getPrice());
    }

    public static List<RestReservaDto> toRestReservaDtos(List<Reserva> reservas){
        List<RestReservaDto> reservaDtos = new ArrayList<>(reservas.size());
        for (int i = 0; i < reservas.size(); i++){
            Reserva reserva = reservas.get(i);
            reservaDtos.add(toRestReservaDto(reserva));
        }
        return reservaDtos;
    }
}