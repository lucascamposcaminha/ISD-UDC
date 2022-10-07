package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientExcursionDtoConversor {

    public static ObjectNode toObjectNode(ClientExcursionDto excursion) throws IOException {

        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();

        if(excursion.getIdExcursion() != null){
            excursionObject.put("idExcursion", excursion.getIdExcursion());
        }

        excursionObject.put("city", excursion.getCity()).
                put("description", excursion.getDescription()).
                put("startDateTime", excursion.getStartDateTime().toString()).
                put("price", excursion.getPrice()).
                put("maxPlaces", excursion.getMaxPlaces()).
                put("freePlaces", excursion.getOccupiedPlaces());
        return excursionObject;
    }

    public static ClientExcursionDto toClientExcursionDto(InputStream jsonExcursion) throws ParsingException{

        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);
            if(rootNode.getNodeType()!= JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }else{
                return toClientExcursionDto(rootNode);
            }
        }catch (ParsingException e){
            throw e;
        }catch (Exception e){
            throw new ParsingException(e);
        }
    }

    public static ClientExcursionDto toClientExcursionDto(JsonNode excursionNode) throws ParsingException{

        if(excursionNode.getNodeType() != JsonNodeType.OBJECT){
            throw new ParsingException("Unrecognized JSON (object expected)");
        }
        else{
            ObjectNode excursionObject = (ObjectNode) excursionNode;

            JsonNode idExcursionNode = excursionObject.get("idExcursion");
            Long idExcursion = (idExcursionNode != null) ? idExcursionNode.longValue() : null;

            String city = excursionObject.get("city").textValue().trim();
            String description = excursionObject.get("description").textValue().trim();
            LocalDateTime startDateTime = LocalDateTime.parse(excursionObject.get("startDateTime").textValue().trim());
            float price = excursionObject.get("price").floatValue();
            int maxPlaces = excursionObject.get("maxPlaces").intValue();
            int freePlaces = excursionObject.get("freePlaces").intValue();

            return new ClientExcursionDto(idExcursion, city, description, startDateTime, price,
                    maxPlaces, maxPlaces - freePlaces);
        }
    }


    public static List<ClientExcursionDto> toClientExcursionDtos(InputStream jsonExcursion) throws ParsingException{

        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode excursionArray = (ArrayNode) rootNode;
                List<ClientExcursionDto> excursionDtos = new ArrayList<>(excursionArray.size());
                for (JsonNode excursionNode : excursionArray){
                    excursionDtos.add(toClientExcursionDto(excursionNode));
                }

                return excursionDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e){
            throw new ParsingException(e);
        }
    }
}