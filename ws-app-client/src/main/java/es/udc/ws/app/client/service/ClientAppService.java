package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientAppService {

    public ClientExcursionDto addExcursion(ClientExcursionDto excursion) throws InputValidationException; // CU 1

    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException, ClientMinimumPlacesException,
            ClientLateUpdateException, InstanceNotFoundException, ClientInvalidDateException; // CU 2

    public List<ClientExcursionDto> findExcursions(String fechaIni, String fechaEnd, String city) // CU 3
            throws InputValidationException;

    public Long reserva(ClientReservaDto reserva) throws InstanceNotFoundException,
            InputValidationException, ClientMaxParticipantsException, ClientLateRegisterException; // CU 4

    public void cancelReserva(Long idReserva, String userEmail) throws ClientLateCancelException, ClientInvalidUserException,
            InstanceNotFoundException, InputValidationException, ClientAlreadyCanceledException; // CU 5

    public List<ClientReservaDto> findUserReservas(String userEmail) throws InputValidationException; // CU 6
}
