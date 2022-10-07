package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.excursion.Excursion;

import java.util.ArrayList;
import java.util.List;

public class ExcursionToRestExcursionDtoConversor {

    public static RestExcursionDto toRestExcursionDto(Excursion excursion) {
        return new RestExcursionDto(excursion.getIdExcursion(), excursion.getCity(),
                excursion.getDescription(), excursion.getStartDateTime(), excursion.getPrice(),
                excursion.getMaxPlaces(), excursion.getFreePlaces());
    }

    public static List<RestExcursionDto> toRestExcursionDtos(List<Excursion> excursions){
        List<RestExcursionDto> excursionDtos = new ArrayList<>(excursions.size());
        for (int i = 0; i < excursions.size(); i++){
            Excursion excursion = excursions.get(i);
            excursionDtos.add(toRestExcursionDto(excursion));
        }
        return excursionDtos;
    }

    public static Excursion toExcursion(RestExcursionDto excursion){
        return new Excursion(excursion.getIdExcursion(), excursion.getCity(), excursion.getDescription(),
                excursion.getStartDateTime(), null, excursion.getPrice(), excursion.getMaxPlaces(),
                excursion.getFreePlaces());
    }
}
