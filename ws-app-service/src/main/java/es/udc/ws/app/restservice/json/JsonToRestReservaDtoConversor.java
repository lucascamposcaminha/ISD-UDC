package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestReservaDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonToRestReservaDtoConversor {

    public static ObjectNode toObjectNode(RestReservaDto reserva){

        ObjectNode reservaNode = JsonNodeFactory.instance.objectNode();

        if(reserva.getIdReserva() != null){
            reservaNode.put("idReserva", reserva.getIdReserva());
        }

        reservaNode.put("idExcursion", reserva.getIdExcursion()).
                put("numPlaces", reserva.getNumPlaces()).
                put("numCreditCard", reserva.getNumCreditCard()).
                put("registerDateTime", reserva.getRegisterDateTime().toString()).
                put("userEmail", reserva.getUserEmail());
        if (reserva.getCancelDateTime() != null) {
            reservaNode.put("cancelDateTime", reserva.getCancelDateTime().toString());
        }
        else {
            reservaNode.put("cancelDateTime", "");
        }
        reservaNode.put("price", reserva.getPrice());

        return reservaNode;
    }

    public static ArrayNode toArrayNode(List<RestReservaDto> reserva){

        ArrayNode reservaNode = JsonNodeFactory.instance.arrayNode();

        for (int i = 0; i < reserva.size(); i++){
            RestReservaDto reservaDto = reserva.get(i);
            ObjectNode reservaObject = toObjectNode(reservaDto);
            reservaNode.add(reservaObject);
        }
        return reservaNode;
    }
}

