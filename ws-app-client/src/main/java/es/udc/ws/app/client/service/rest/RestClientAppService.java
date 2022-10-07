package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExcursionDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientReservaDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class RestClientAppService implements ClientAppService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientAppService.endpointAddress";
    private String endpointAddress;

    // Caso de uso 1 - Lucas
    @Override
    public ClientExcursionDto addExcursion(ClientExcursionDto excursion) throws InputValidationException {
        try{
            HttpResponse response = Request.Post(getEndpointAddress() + "excursions").
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);
            return JsonToClientExcursionDtoConversor.toClientExcursionDto(response.getEntity().getContent());
        }
        catch (InputValidationException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 2 - Jose
    @Override
    public void updateExcursion(ClientExcursionDto excursion) throws ClientMinimumPlacesException, ClientLateUpdateException,
            InstanceNotFoundException, InputValidationException, ClientInvalidDateException {
        try {
            HttpResponse response = Request.Put(getEndpointAddress() + "excursions/" + excursion.getIdExcursion()).
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
        }
        catch (InputValidationException | InstanceNotFoundException | ClientInvalidDateException |
                ClientLateUpdateException | ClientMinimumPlacesException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 3 - Elena
    @Override
    public List<ClientExcursionDto> findExcursions(String city, String fechaIni, String fechaEnd) throws InputValidationException {

        try{
            HttpResponse response = Request.Get(getEndpointAddress() + "excursions/?city=" + URLEncoder.encode(city, "UTF-8") +
                    "&ini=" + URLEncoder.encode(fechaIni, "UTF-8") +
                    "&end=" + URLEncoder.encode(fechaEnd, "UTF-8")).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientExcursionDtoConversor.toClientExcursionDtos(response.getEntity()
                    .getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 4 - Lucas
    @Override
        public Long reserva(ClientReservaDto reserva)
            throws InstanceNotFoundException, InputValidationException, ClientMaxParticipantsException, ClientLateRegisterException {

        try{
            HttpResponse response = Request.Post(getEndpointAddress() + "reservas").
                    bodyForm(
                            Form.form()
                                    .add("userEmail", reserva.getUserEmail())
                                    .add("idExcursion", Long.toString(reserva.getIdExcursion()))
                                    .add("numCreditCard", reserva.getNumCreditCard())
                                    .add("numPlaces", Integer.toString(reserva.getNumPlaces()))
                                    .build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientReservaDtoConversor.toClientReservaDto(response.getEntity().getContent());
        }
        catch (InputValidationException | InstanceNotFoundException | ClientMaxParticipantsException | ClientLateRegisterException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 5 - Jose
    @Override
    public void cancelReserva(Long idReserva, String userEmail) throws ClientLateCancelException, ClientInvalidUserException,
            InstanceNotFoundException, InputValidationException, ClientAlreadyCanceledException {

        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "reservas/" + idReserva + "/cancel")
                    .bodyForm(Form.form().add("userEmail", userEmail).build()).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
        }
        catch (InputValidationException | InstanceNotFoundException | ClientAlreadyCanceledException |
                ClientLateCancelException | ClientInvalidUserException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 6 - Elena
    @Override
    public List<ClientReservaDto> findUserReservas(String userEmail) throws InputValidationException{
        try{
            HttpResponse response = Request.Get(getEndpointAddress() + "reservas?userEmail="
                    + URLEncoder.encode(userEmail,"UTF-8")).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientReservaDtoConversor.toClientReservaDtos(response.getEntity().getContent());
        }catch (InputValidationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientExcursionDto excursion) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientExcursionDtoConversor.toObjectNode(excursion));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateStatusCode(int successCode, org.apache.http.HttpResponse response) throws Exception {
        try {
            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }
            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());
                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
