package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.InvalidDateException;
import es.udc.ws.app.model.excursionservice.exceptions.LateUpdateException;
import es.udc.ws.app.model.excursionservice.exceptions.MinimumPlacesException;
import es.udc.ws.app.restservice.dto.ExcursionToRestExcursionDtoConversor;
import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestExcursionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ExcursionsServlet extends RestHttpServletTemplate {

    // FUNC-1 añadir excursión
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, InputValidationException {

        ServletUtils.checkEmptyPath(req);
        
        RestExcursionDto excursionDto = JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());
        Excursion excursion = ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);

        excursion = ExcursionServiceFactory.getService().addExcursion(excursion);

        excursionDto = ExcursionToRestExcursionDtoConversor.toRestExcursionDto(excursion);
        String excursionURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + excursion.getIdExcursion();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", excursionURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestExcursionDtoConversor.toObjectNode(excursionDto), headers);
    }

    // FUNC-2 actualizar excursión
    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, InputValidationException, InstanceNotFoundException {

        Long idExcursion = ServletUtils.getIdFromPath(req, "excursion");

        RestExcursionDto excursionDto = JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());

        if (!idExcursion.equals(excursionDto.getIdExcursion())) {
            throw new InputValidationException("Petición inválida: el id de la excursión introducido no es válido");
        }
        Excursion excursion = ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);

        try {
            ExcursionServiceFactory.getService().updateExcursion(excursion);
        } catch (InvalidDateException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toInvalidDateException(e), null);
        } catch (LateUpdateException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toLateUpdateException(e), null);
        } catch (MinimumPlacesException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toMinimumPlacesException(e), null);
        }
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

    // FUNC-3 buscar excursiones
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, InputValidationException {

        ServletUtils.checkEmptyPath(req);
        LocalDate ini = LocalDate.parse(req.getParameter("ini"));
        LocalDate end = LocalDate.parse(req.getParameter("end"));
        String city = req.getParameter("city");

        List<Excursion> excursions = ExcursionServiceFactory.getService().findExcursions(ini, end, city);
        List<RestExcursionDto> excursionDtos = ExcursionToRestExcursionDtoConversor.toRestExcursionDtos(excursions);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestExcursionDtoConversor.toArrayNode(excursionDtos), null);
    }
}
