package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.restservice.dto.ReservaToRestReservaDtoConversor;
import es.udc.ws.app.restservice.dto.RestReservaDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestReservaDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ReservasServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException,
            InstanceNotFoundException {

        // FUNC-4 reservar
        if (req.getPathInfo() == null) {

            ServletUtils.checkEmptyPath(req);
            String userEmail = ServletUtils.getMandatoryParameter(req, "userEmail").trim();
            String numCreditCard = ServletUtils.getMandatoryParameter(req, "numCreditCard").trim();
            Long idExcursion = ServletUtils.getMandatoryParameterAsLong(req, "idExcursion");
            int numPlaces = Integer.parseInt(ServletUtils.getMandatoryParameter(req, "numPlaces"));

            Reserva reserva;

            try {
                reserva = ExcursionServiceFactory.getService().reserva(userEmail, idExcursion, numCreditCard, numPlaces);
                RestReservaDto reservaDto = ReservaToRestReservaDtoConversor.toRestReservaDto(reserva);

                String reservaURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + reserva.getIdExcursion().toString();
                Map<String, String> headers = new HashMap<>(1);
                headers.put("Location", reservaURL);

                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                        JsonToRestReservaDtoConversor.toObjectNode(reservaDto), headers);
            }
            catch (LateRegisterException e){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toLateRegisterException(e), null);
            }
            catch (MaxParticipantsException e){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toMaxParticipantsException(e), null);
            }
        }
        else { // FUNC-5 cancelar reserva
            String pathInfo = req.getPathInfo();
            String[] splittedInfo = pathInfo.split("/");

            if (!splittedInfo[2].equals("cancel")) {
                throw new InputValidationException("Operación no reconocida");
            }

            Long idReserva = Long.valueOf(splittedInfo[1]);
            String userEmail = ServletUtils.getMandatoryParameter(req, "userEmail").trim();

            try {
                ExcursionServiceFactory.getService().cancelReserva(idReserva, userEmail);
            } catch (LateCancelException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toLateCancelException(e), null);
            } catch (InvalidUserException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toInvalidUserException(e), null);
            } catch (AlreadyCanceledException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toAlreadyCanceledException(e), null);
            }
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
        }
    }

    // FUNC-6 encontrar reservas de un usuario
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException{
        ServletUtils.checkEmptyPath(req);
        String userEmail = req.getParameter("userEmail");

        List<Reserva> reservas = ExcursionServiceFactory.getService().findUserReservas(userEmail);

        List<RestReservaDto> reservaDtos = ReservaToRestReservaDtoConversor.toRestReservaDtos(reservas);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestReservaDtoConversor.toArrayNode(reservaDtos), null);
    }

}
