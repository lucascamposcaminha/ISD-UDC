package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.thrift.ThriftExcursionDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExcursionToThriftExcursionDtoConversor {

    public static Excursion toExcursion(ThriftExcursionDto excursion){
        return new Excursion(excursion.getIdExcursion(), excursion.getCity(), excursion.getDescription(),
                LocalDateTime.parse(excursion.getStartDateTime()), (float) excursion.getPrice(), excursion.getMaxPlaces());
    }

    public static ThriftExcursionDto toThriftExcursionDto(Excursion excursion){
        return new ThriftExcursionDto(excursion.getIdExcursion(), excursion.getCity(), excursion.getDescription(),
                excursion.getStartDateTime().toString(), excursion.getPrice(), excursion.getMaxPlaces(), excursion.getFreePlaces());
    }

}
