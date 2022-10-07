package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestExcursionDtoConversor {

    public static ObjectNode toObjectNode(RestExcursionDto excursion){

        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();

        if(excursion.getIdExcursion() != null){
            excursionObject.put("idExcursion", excursion.getIdExcursion());
        }

        excursionObject.put("city", excursion.getCity()).
                put("description", excursion.getDescription()).
                put("startDateTime", excursion.getStartDateTime().toString()).
                put("price", excursion.getPrice()).
                put("maxPlaces", excursion.getMaxPlaces()).
                put("freePlaces", excursion.getFreePlaces());

        return excursionObject;
    }

    public static ArrayNode toArrayNode(List<RestExcursionDto> excursions) {

        ArrayNode excursionsNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < excursions.size(); i++) {
            RestExcursionDto excursionDto = excursions.get(i);
            ObjectNode excursionObject = toObjectNode(excursionDto);
            excursionsNode.add(excursionObject);
        }

        return excursionsNode;
    }

    public static RestExcursionDto toRestExcursionDto(InputStream jsonExcursion) throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);

            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }
            else{
                ObjectNode excursionObject = (ObjectNode) rootNode;
                JsonNode excursionIdNode = excursionObject.get("idExcursion");

                Long idExcursion = (excursionIdNode != null) ? excursionIdNode.longValue() : null;

                String city = excursionObject.get("city").textValue().trim();
                String description = excursionObject.get("description").textValue().trim();
                LocalDateTime startDateTime = LocalDateTime.parse(excursionObject.get("startDateTime").textValue().trim());
                float price = excursionObject.get("price").floatValue();
                int maxPlaces = excursionObject.get("maxPlaces").intValue();

                return new RestExcursionDto(idExcursion, city, description, startDateTime, price, maxPlaces);
            }
        }
        catch (ParsingException ex) {
            throw ex;
        }
        catch (Exception e){
            throw new ParsingException(e);
        }
    }
}
