package es.udc.ws.app.client.service.thrift;

import com.sun.java.accessibility.util.AccessibilityListenerList;
import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

public class ThriftClientAppService implements ClientAppService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientAppService.endpointAddress";
    private final static String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    //Lucas
    @Override
    public ClientExcursionDto addExcursion(ClientExcursionDto excursion) throws InputValidationException {

        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try{
            transport.open();
            ThriftExcursionDto addedExcursion = client.addExcursion(ClientExcursionDtoToThriftExcursionDtoConversor.toThriftExcursionDto(excursion));
            return ClientExcursionDtoToThriftExcursionDtoConversor.toClientExcursionDto(addedExcursion);
        }
        catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        finally {
            transport.close();
        }
    }

    //Lucas
    @Override
    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException,
            ClientMinimumPlacesException, ClientLateUpdateException, InstanceNotFoundException, ClientInvalidDateException {

        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try{
            transport.open();
            client.updateExcursion(ClientExcursionDtoToThriftExcursionDtoConversor.toThriftExcursionDto(excursion));
        }
        catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        }
        catch (ThriftMinimumPlacesException e){
            throw new ClientMinimumPlacesException(e.getMessage());
        }
        catch (ThriftLateUpdateException e){
            throw new ClientLateUpdateException(e.getMessage());
        }
        catch (ThriftInvalidDateException e){
            throw new ClientInvalidDateException(e.getMessage());
        }
        catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientExcursionDto> findExcursions(String fechaIni, String fechaEnd, String city) throws InputValidationException {
        return null;
    }

    @Override
    public Long reserva(ClientReservaDto reserva) throws InstanceNotFoundException, InputValidationException, ClientMaxParticipantsException, ClientLateRegisterException {
        return null;
    }

    @Override
    public void cancelReserva(Long idReserva, String userEmail) throws ClientLateCancelException, ClientInvalidUserException, InstanceNotFoundException, InputValidationException, ClientAlreadyCanceledException {

    }

    @Override
    public List<ClientReservaDto> findUserReservas(String userEmail) throws InputValidationException {
        return null;
    }

    private ThriftExcursionService.Client getClient(){

        try{
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);
            return new ThriftExcursionService.Client(protocol);
        }
        catch (TTransportException e){
            throw new RuntimeException(e);
        }
    }

}
