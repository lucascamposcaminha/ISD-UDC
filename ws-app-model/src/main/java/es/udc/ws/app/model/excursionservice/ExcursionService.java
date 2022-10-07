package es.udc.ws.app.model.excursionservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ExcursionService {

    Excursion addExcursion(Excursion excursion) throws InputValidationException;

    void updateExcursion(Excursion updatedExcursion)
            throws InputValidationException, InstanceNotFoundException, InvalidDateException,
            LateUpdateException, MinimumPlacesException;

    List<Excursion> findExcursions(LocalDate ini, LocalDate end, String city)
        throws InputValidationException;

    Reserva reserva(String userEmail, Long idExcursion, String numCreditCard, int numPlaces)
        throws InstanceNotFoundException, InputValidationException, MaxParticipantsException, LateRegisterException;

    Reserva cancelReserva(Long idReserva, String userEmail)
        throws InstanceNotFoundException, InputValidationException, LateCancelException, InvalidUserException,
            AlreadyCanceledException;

    List<Reserva> findUserReservas(String userEmail) throws InputValidationException;
}