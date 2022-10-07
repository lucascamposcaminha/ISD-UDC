package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.thrift.ThriftExcursionDto;

import java.time.LocalDateTime;

public class ClientExcursionDtoToThriftExcursionDtoConversor {

    public static ThriftExcursionDto toThriftExcursionDto(ClientExcursionDto clientExcursionDto){

        Long idExcursion = clientExcursionDto.getIdExcursion();
        return new ThriftExcursionDto(idExcursion == null ? -1 : idExcursion.longValue(), clientExcursionDto.getCity(),
                clientExcursionDto.getDescription(), clientExcursionDto.getStartDateTime() + ":00",
                clientExcursionDto.getPrice(), clientExcursionDto.getMaxPlaces(),
                clientExcursionDto.getMaxPlaces() - clientExcursionDto.getOccupiedPlaces());

    }

    public static ClientExcursionDto toClientExcursionDto(ThriftExcursionDto excursion){
        return new ClientExcursionDto(excursion.getIdExcursion(), excursion.getCity(), excursion.getDescription(),
                LocalDateTime.parse(excursion.getStartDateTime()), (float) excursion.getPrice(), excursion.getMaxPlaces(),
                excursion.getMaxPlaces() - excursion.getFreePlaces());
    }

}
