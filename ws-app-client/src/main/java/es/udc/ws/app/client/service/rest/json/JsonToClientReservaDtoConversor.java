package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientReservaDtoConversor {

    public static Long toClientReservaDto(InputStream jsonReserva) throws ParsingException{

        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);
            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }
            else{
                ObjectNode reservaObject = (ObjectNode) rootNode;

                JsonNode idReservaNode = reservaObject.get("idReserva");

                return (idReservaNode != null) ? idReservaNode.longValue() : null;
            }
        }
        catch (ParsingException ex){
            throw ex;
        }
        catch (Exception e){
            throw new ParsingException(e);
        }
    }

    public static List<ClientReservaDto> toClientReservaDtos(InputStream jsonExcursion) throws ParsingException{
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);

            if (rootNode.getNodeType() != JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode reservaArray = (ArrayNode) rootNode;
                List<ClientReservaDto> reservasDto = new ArrayList<>(reservaArray.size());

                for (JsonNode reservaNode : reservaArray){
                    reservasDto.add(toClientReservaDto(reservaNode));
                }
                return reservasDto;
            }
        }catch (ParsingException ex) {
            throw ex;

        }catch (Exception e){
            throw new ParsingException(e);
        }
    }

    private static ClientReservaDto toClientReservaDto(JsonNode reservaNode) throws ParsingException{

        if (reservaNode.getNodeType() != JsonNodeType.OBJECT){
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode reservaObject = (ObjectNode) reservaNode;

            JsonNode reservaIdNode = reservaObject.get("idReserva");
            Long idReserva = (reservaIdNode != null) ? reservaIdNode.longValue() : null;

            Long idExcursion = reservaObject.get("idExcursion").asLong();
            String userEmail = reservaObject.get("userEmail").asText().trim();
            int numPlaces = reservaObject.get("numPlaces").asInt();
            String numCreditCard = reservaObject.get("numCreditCard").asText().trim();
            LocalDateTime registerDateTime = LocalDateTime.parse(reservaObject.get("registerDateTime").asText().trim());
            String cancelDateTimeNode = reservaObject.get("cancelDateTime").asText();
            LocalDateTime cancelDateTime = (!cancelDateTimeNode.equals("")) ? LocalDateTime.parse(reservaObject.get("cancelDateTime").textValue().trim()) : null;
            float price = reservaObject.get("price").floatValue();

            return new ClientReservaDto(idReserva, idExcursion, userEmail, numPlaces, numCreditCard, registerDateTime,
                    cancelDateTime, price);
        }
    }
}