package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientAppService;
import es.udc.ws.app.client.service.ClientAppServiceFactory;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {

        if(args.length == 0){
            printUsageAndExit();
        }
        ClientAppService clientAppService = ClientAppServiceFactory.getService();

        if("-addExc".equalsIgnoreCase(args[0])){
            validateArgs(args, 6, new int[] {5});
            //-addExc <city> <description> <startDateTime> <price> <maxPlaces>

            try{
                ClientExcursionDto idExcursion = clientAppService.addExcursion(new ClientExcursionDto(args[1], args[2],
                        LocalDateTime.parse(args[3]), Float.valueOf(args[4]), Integer.parseInt(args[5])));

                System.out.println("Excursión " + idExcursion.getIdExcursion() + " creada correctamente\n");
            }
            catch (NumberFormatException | InputValidationException ex){
                ex.printStackTrace(System.err);
            }
            catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        }
        else if ("-updateExc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 7, new int[] {1, 5, 6});
            //-updateExc <idExcursion> <city> <description> <startDateTime> <price> <maxPlaces>

            try {
                clientAppService.updateExcursion(new ClientExcursionDto(
                        Long.valueOf(args[1]), args[2], args[3], LocalDateTime.parse(args[4]),
                        Float.parseFloat(args[5]), Integer.parseInt(args[6])
                ));
                System.out.println("La excursión " + args[1] + " ha sido actualizada satisfactoriamente\n");
            }
            catch (InputValidationException | InstanceNotFoundException | NumberFormatException |
                    ClientLateUpdateException | ClientInvalidDateException | ClientMinimumPlacesException ex) {
                ex.printStackTrace(System.err);
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        else if("-findExcursion".equalsIgnoreCase(args[0])){
            validateArgs(args, 4, new int[]{});
            //-findExcursion <city> <ini> <end>

            try{
                List<ClientExcursionDto> excursions = clientAppService.findExcursions(args[1],args[2],args[3]);
                System.out.println("Encontrada(s) " + excursions.size() +
                        " excursion(es) en " + args[1] + " entre las fechas '"
                        + args[2] + "' y '" + args[3] + "':\n");

                for (int i = 0; i < excursions.size(); i++) {
                    ClientExcursionDto excursionDto = excursions.get(i);
                    System.out.println("Id: " + excursionDto.getIdExcursion() +
                            ", Ciudad: " + excursionDto.getCity() +
                            ", Descripción: " + excursionDto.getDescription() +
                            ", Fecha: " + excursionDto.getStartDateTime() +
                            ", Precio: " + excursionDto.getPrice() +
                            ", Maximo Plazas: " + excursionDto.getMaxPlaces() +
                            ", Plazas Reservadas: " + excursionDto.getOccupiedPlaces() + "\n");
                }
            }
            catch (InputValidationException ex) {
                ex.printStackTrace(System.err);
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        else if("-reserve".equalsIgnoreCase(args[0])){
            validateArgs(args, 5, new int[] {4});
            //-reserve <userEmail> <idExcursion> <numCreditCard> <numPlaces>

            try{
                Long idReserva = clientAppService.reserva(new ClientReservaDto(Long.valueOf(args[2]),args[1],
                        Integer.parseInt(args[4]), args[3]));
                System.out.println("Reserva " + idReserva + " realizada correctamente\n");
            }
            catch (NumberFormatException | InputValidationException | ClientLateRegisterException | ClientMaxParticipantsException ex){
                ex.printStackTrace(System.err);
            }
            catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        }
        else if("-cancel".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {1});
            //-cancel <idReserva> <userEmail>

            try {
                clientAppService.cancelReserva(Long.valueOf(args[1]), args[2]);
                System.out.println("La reserva " + args[1] + " fue cancelada satisfactoriamente\n");
            }
            catch (InputValidationException | InstanceNotFoundException | NumberFormatException |
                    ClientLateCancelException | ClientInvalidUserException | ClientAlreadyCanceledException ex) {
                ex.printStackTrace(System.err);
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        else if("-findUserReservas".equalsIgnoreCase(args[0])){
            validateArgs(args, 2, new int[] {});
            //-findUserReservas <userEmail> -- DECIDID COMO MANDAR EL COMANDO ESTE

            try {
                List<ClientReservaDto> reservas = clientAppService.findUserReservas(args[1]);
                System.out.println("Encontrada(s) " + reservas.size() +
                        " reserva(s) del usuario " + args[1] + "\n");

                for (int i = 0; i < reservas.size(); i++) {
                    ClientReservaDto reservaDto = reservas.get(i);
                    System.out.println("Id: " + reservaDto.getIdReserva() +
                            ", Id excursion: " + reservaDto.getIdExcursion() +
                            ", Email: " + reservaDto.getUserEmail() +
                            ", Numero personas: " + reservaDto.getNumPlaces() +
                            ", Numero tarjeta bancaria: " + reservaDto.getNumCreditCard() +
                            ", Fecha de la reserva: " + reservaDto.getRegisterDateTime() +
                            ", Fecha de cancelación: " + reservaDto.getCancelDateTime() +
                            ", Precio: " +reservaDto.getPrice() + "\n");
                }
            }
            catch (InputValidationException ex) {
                ex.printStackTrace(System.err);
            }
            catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments){

        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            }
            catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit(){
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usos:\n" +
                "    [add excursion] -addExc <city> <description> <startDateTime> <price> <maxPlaces>\n" +
                "    [update excursion] -updateExc <idExcursion> <city> <description> <startDateTime> <price> <maxPlaces>\n" +
                "    [find excursions] -findExcursion <city> <ini> <end>\n" +
                "    [reserva] -reserve <userEmail> <idExcursion> <numCreditCard> <numPlaces>\n" +
                "    [cancel reserva] -cancel <idReserva> <userEmail>\n" +
                "    [find userReservas] -findUserReservas <userEmail>\n");
    }
}