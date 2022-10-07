package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.excursionservice.exceptions.*;

public class AppExceptionToJsonConversor {

    public static ObjectNode toAlreadyCanceledException(AlreadyCanceledException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyCanceled");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toInvalidDateException(InvalidDateException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidDate");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toInvalidUserException(InvalidUserException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidUser");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toLateRegisterException(LateRegisterException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LateRegister");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toLateCancelException(LateCancelException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LateCancel");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toLateUpdateException(LateUpdateException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LateUpdate");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toMaxParticipantsException(MaxParticipantsException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "MaxParticipants");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toMinimumPlacesException(MinimumPlacesException exception) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "MinimumPlaces");
        exceptionObject.put("message", exception.getMessage());

        return exceptionObject;
    }
}