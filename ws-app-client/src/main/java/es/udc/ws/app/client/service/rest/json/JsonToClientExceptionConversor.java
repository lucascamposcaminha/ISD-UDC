package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Error no reconocido: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                }
                else {
                    throw new ParsingException("Error no reconocido: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if(errorType.equals("LateRegister")){
                    return toLateRegisterException(rootNode);
                }
                else if (errorType.equals("InvalidDate")) {
                    return toInvalidDateException(rootNode);
                }
                else if (errorType.equals("MinimumPlaces")) {
                    return toMinimumPlacesException(rootNode);
                }
                else if (errorType.equals("InvalidUser")) {
                    return toInvalidUserException(rootNode);
                }
                else if (errorType.equals("LateCancel")) {
                    return toLateCancelException(rootNode);
                }
                else if (errorType.equals("AlreadyCanceled")) {
                    return toAlreadyCanceledException(rootNode);
                }
                else if (errorType.equals("LateUpdate")){
                    return toLateUpdateException(rootNode);
                }
                else if (errorType.equals("MaxParticipants")){
                    return toMaxParticipantException(rootNode);
                }
                else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    private static ClientMaxParticipantsException toMaxParticipantException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientMaxParticipantsException("Error " + instanceId + ": " + instanceType);
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    private static ClientLateRegisterException toLateRegisterException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientLateRegisterException("Error " + instanceId + ": " + instanceType);
    }

    private static ClientAlreadyCanceledException toAlreadyCanceledException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientAlreadyCanceledException("Error " + instanceId + ": " + instanceType);
    }

    private static ClientInvalidDateException toInvalidDateException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientInvalidDateException("Error " + instanceId + ": " + instanceType);
    }

    private static ClientInvalidUserException toInvalidUserException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientInvalidUserException("Error " + instanceId + ": " + instanceType);
    }

    private static ClientLateCancelException toLateCancelException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientLateCancelException("Error " + instanceId + ": " + instanceType);
    }

    private static ClientLateUpdateException toLateUpdateException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientLateUpdateException("Error " + instanceId + ": " + instanceType);
    }

    private static ClientMinimumPlacesException toMinimumPlacesException(JsonNode rootNode){
        String instanceId = rootNode.get("errorType").textValue();
        String instanceType = rootNode.get("message").textValue();
        return new ClientMinimumPlacesException("Error " + instanceId + ": " + instanceType);
    }
}
