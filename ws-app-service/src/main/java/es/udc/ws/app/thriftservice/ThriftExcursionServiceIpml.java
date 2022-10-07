package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.InvalidDateException;
import es.udc.ws.app.model.excursionservice.exceptions.LateUpdateException;
import es.udc.ws.app.model.excursionservice.exceptions.MinimumPlacesException;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.restservice.dto.ReservaToRestReservaDtoConversor;
import es.udc.ws.app.thriftservice.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ThriftExcursionServiceIpml implements ThriftExcursionService.Iface {

    @Override
    public ThriftExcursionDto addExcursion(ThriftExcursionDto excursionDto) throws ThriftInputValidationException {

        Excursion excursion = ExcursionToThriftExcursionDtoConversor.toExcursion(excursionDto);

        try{
            Excursion addedExcursion = ExcursionServiceFactory.getService().addExcursion(excursion);
            return ExcursionToThriftExcursionDtoConversor.toThriftExcursionDto(addedExcursion);
        }
        catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public void updateExcursion(ThriftExcursionDto excursionDto) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftMinimumPlacesException, ThriftInvalidDateException, ThriftLateUpdateException {

        Excursion excursion = ExcursionToThriftExcursionDtoConversor.toExcursion(excursionDto);

        try{
            ExcursionServiceFactory.getService().updateExcursion(excursion);
        }
        catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
        catch (InstanceNotFoundException e){
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
        catch (MinimumPlacesException e){
            throw new ThriftMinimumPlacesException(e.getMessage());
        }
        catch (InvalidDateException e){
            throw new ThriftInvalidDateException(e.getMessage());
        }
        catch (LateUpdateException e){
            throw new ThriftLateUpdateException(e.getMessage());
        }
    }

}
