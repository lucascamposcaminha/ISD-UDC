package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.app.model.excursionservice.ExcursionService;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.app.model.util.ModelConstants;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {

    private final long NON_EXISTENT_EXC_ID = -1;
    private final long NON_EXISTENT_RES_ID = -1;
    private final String userEmail = "ws-user@app.es";
    private final String numCreditCard = "6516661315195155";

    private static DataSource dataSource = null;
    private static ExcursionService excursionService = null;
    private static SqlExcursionDao excursionDao = null;
    private static SqlReservaDao reservaDao = null;

    @BeforeAll
    public static void init() {
        dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        excursionService = ExcursionServiceFactory.getService();
        excursionDao = SqlExcursionDaoFactory.getDao();
        reservaDao = SqlReservaDaoFactory.getDao();
    }

    private Excursion getValidExcursion() {
        return new Excursion("A Coruña", "Description", LocalDateTime.of(2027, Month.JULY, 15, 15, 30),
                10, 75);
    }

    private Excursion createExcursion(Excursion excursion){

        Excursion addExcursion;
        try{
            addExcursion = excursionService.addExcursion(excursion);
        }
        catch (InputValidationException e){
            throw new RuntimeException(e);
        }
        return addExcursion;
    }

    private Excursion findExcursion(Long idExcursion) {

        Excursion excursionFound;
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                excursionFound = excursionDao.find(connection, idExcursion);

                return excursionFound;

            } catch (InstanceNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removeExcursion(Long idExcursion){

        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                excursionDao.remove(connection, idExcursion);
                connection.commit();
            }
            catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            }
            catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }
            catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Reserva findReserva(Long idReserva){

        Reserva reservaFound;
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                reservaFound = reservaDao.find(connection, idReserva);

                return reservaFound;
            }
            catch (InstanceNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removeReserva(Long IdReserva){
        DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.APP_DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()){
            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                reservaDao.remove(connection, IdReserva);
                connection.commit();
            }
            catch (InstanceNotFoundException e){
                connection.commit();
                throw new RuntimeException(e);
            }
            catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }
            catch (RuntimeException|Error e){
                connection.rollback();
                throw e;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Usamos esta función para poder actualizar excursiones de formas que no podríamos usando la función del servicio
    private void updateExcursion(Excursion excursion) {

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                excursionDao.update(connection, excursion);
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Caso de uso 1 - Lucas
    // addExcursion
    // CP-1: Añadir excursion sin fallos
    @Test
    public void testAddExcursion() throws InputValidationException {

        Excursion excursion = getValidExcursion();
        Excursion addedExcursion = null;

        try {
            addedExcursion = excursionService.addExcursion(excursion);
            Excursion foundExcursion = findExcursion(addedExcursion.getIdExcursion());
            assertEquals(addedExcursion, foundExcursion);
        }
        finally {
            if (addedExcursion != null) {
                removeExcursion(addedExcursion.getIdExcursion());
            }
        }
    }

    // CP-2: Añadir excursión pero con campos inválidos
    @Test
    public void testAddInvalidExcursion() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //city null
        assertThrows(InputValidationException.class, () ->{
            Excursion excursion = getValidExcursion();
            excursion.setCity("");
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getIdExcursion());
        });

        //description null
        assertThrows(InputValidationException.class, () ->{
            Excursion excursion = getValidExcursion();
            excursion.setDescription("");
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getIdExcursion());
        });

        //invalid date
        assertThrows(InputValidationException.class, () ->{
            Excursion excursion = getValidExcursion();
            excursion.setStartDateTime(LocalDateTime.parse("2022-06-12 01:00:00", formatter));
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getIdExcursion());
        });

        //price < 0
        assertThrows(InputValidationException.class, () ->{
            Excursion excursion = getValidExcursion();
            excursion.setPrice(-5);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getIdExcursion());
        });

        //maxPlaces = 0
        assertThrows(InputValidationException.class, () ->{
            Excursion excursion = getValidExcursion();
            excursion.setMaxPlaces(0);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getIdExcursion());
        });

        //maxPlaces < 0
        assertThrows(InputValidationException.class, () ->{
            Excursion excursion = getValidExcursion();
            excursion.setMaxPlaces(-5);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getIdExcursion());
        });

        //higher maxPlaces
        assertThrows(InputValidationException.class, () ->{
            Excursion excursion = getValidExcursion();
            excursion.setMaxPlaces(MAX_PLACES + 1);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getIdExcursion());
        });
    }


    // Caso de uso 2 - Jose
    // updateExcursion
    // CP-1: Actualizar excursión correctamente
    @Test
    public void testValidUpdateExcursion() throws InvalidDateException, InstanceNotFoundException,
            LateUpdateException, MinimumPlacesException, InputValidationException {

        Excursion excursion = createExcursion(getValidExcursion());

        try {
            Excursion excursionToUpdate = new Excursion(excursion.getIdExcursion(), "nueva ciudad", "nueva descripción",
                    excursion.getStartDateTime().plusHours(4), 6, excursion.getMaxPlaces()-2);

            excursionService.updateExcursion(excursionToUpdate);
            Excursion updatedExcursion = findExcursion(excursion.getIdExcursion());

            excursionToUpdate.setRegisterDateTime(excursion.getRegisterDateTime());
            assertEquals(updatedExcursion, excursionToUpdate);
        }
        finally {
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-2: Intentamos actualizar una excursión inexistente
    @Test
    public void testUpdateNonExistentExcursion() {

        Excursion excursion = getValidExcursion();
        excursion.setIdExcursion(NON_EXISTENT_EXC_ID);
        excursion.setMaxPlaces(excursion.getMaxPlaces()+10);

        assertThrows(InstanceNotFoundException.class, () -> excursionService.updateExcursion(excursion));

    }

    // CP-3: Intentamos actualizar una excursión inválida
    @Test
    public void testUpdateInvalidExcursion() {

        Long idExcursion = createExcursion(getValidExcursion()).getIdExcursion();

        try {

            assertThrows(InputValidationException.class, () -> {
                Excursion excursion = findExcursion(idExcursion);
                excursion.setCity(null);
                excursionService.updateExcursion(excursion);
            });

            assertThrows(InputValidationException.class, () -> {
                Excursion excursion = findExcursion(idExcursion);
                excursion.setDescription(null);
                excursionService.updateExcursion(excursion);
            });

            assertThrows(InputValidationException.class, () -> {
                Excursion excursion = findExcursion(idExcursion);
                excursion.setPrice(-1);
                excursionService.updateExcursion(excursion);
            });

            assertThrows(InputValidationException.class, () -> {
                Excursion excursion = findExcursion(idExcursion);
                excursion.setMaxPlaces(0);
                excursionService.updateExcursion(excursion);
            });
        }
        finally {
            removeExcursion(idExcursion);
        }
    }

    // CP-4: Tratamos de actualizar una excursión cuando quedan menos de 72h para su comienzo
    @Test
    public void testLateUpdateExcursion() {

        Excursion excursion = createExcursion(new Excursion("Barcelona", "Visita a la Sagrada Familia",
                LocalDateTime.now().withNano(0).plusHours(72).plusSeconds(5), 10, 100));

        try {
            excursion.setStartDateTime(excursion.getStartDateTime().withNano(0).minusHours(1));
            updateExcursion(excursion);

            Excursion excursionToUpdate = new Excursion(excursion.getIdExcursion(), "nueva", "nueva desc",
                    excursion.getStartDateTime().withNano(0).plusHours(10), 15, 100);

            assertThrows(LateUpdateException.class, () ->
                    excursionService.updateExcursion(excursionToUpdate));
        }
        finally {
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-5: Intentamos adelantar la excursión
    @Test
    public void testAnticipateStartExcursion() {

        Excursion excursion = createExcursion(getValidExcursion());

        try {
            Excursion excursionToUpdate = new Excursion(excursion.getIdExcursion(), excursion.getCity(), excursion.getDescription(),
                    excursion.getStartDateTime().minusHours(10), excursion.getPrice(), excursion.getMaxPlaces());

            assertThrows(InvalidDateException.class, () ->
                    excursionService.updateExcursion(excursionToUpdate));
        }
        finally {
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-6: Rebajamos el número de plazas disponibles por debajo de las ya reservadas
    @Test
    public void testUpdateExcursionInsufficientPlaces() {

        Excursion excursion = createExcursion(getValidExcursion());
        int reservedPlaces = 25;

        try {
            excursion.setFreePlaces(excursion.getFreePlaces()-reservedPlaces);
            updateExcursion(excursion);

            Excursion excursionToUpdate = new Excursion(excursion.getIdExcursion(), "nueva", "nueva desc",
                    excursion.getStartDateTime().withNano(0).plusHours(10), 15, reservedPlaces-1);

            assertThrows(MinimumPlacesException.class, () ->
                    excursionService.updateExcursion(excursionToUpdate));
        }
        finally {
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // Caso de uso 3 - Elena
    // findExcursions
    // CP-1: Encontrar excursiones especificando dos fechas
    @Test
    public void testFindExcursionsWithTwoDates() throws InputValidationException{

        List<Excursion> excursionList = new ArrayList<>();
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //Excursion 1
        Excursion excursion1 = createExcursion(new Excursion("A Coruña", "Excursion al Domus",
                LocalDateTime.parse("2022-07-21 09:00:00", formatterDateTime), 15, 75));
        excursionList.add(excursion1);
        //Excursion 2
        Excursion excursion2 = createExcursion(new Excursion("Vigo", "Paseo por Castrelos",
                LocalDateTime.parse("2022-07-24 18:15:00", formatterDateTime), 7.5f, 100));
        excursionList.add(excursion2);
        //Excursion 3
        Excursion excursion3 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la catedral",
                LocalDateTime.parse("2022-07-28 18:15:00", formatterDateTime), 10, 50));
        excursionList.add(excursion3);
        //Excursion 4
        Excursion excursion4 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la ciudad de la cultura",
                LocalDateTime.parse("2022-08-24 18:15:00", formatterDateTime), 10, 50));
        excursionList.add(excursion4);

        try{
            List<Excursion> foundExcursions1 = excursionService.findExcursions(LocalDate.of(2022, 7, 1),
                    LocalDate.of(2022,9,28), "A Coruña");
            assertEquals(1, foundExcursions1.size());

            List<Excursion> foundExcursions2 = excursionService.findExcursions(LocalDate.of(2022,7,1),
                    LocalDate.of(2022,7,30), "Santiago de Compostela");
            assertEquals(1, foundExcursions2.size());

            List<Excursion> foundExcursions3 = excursionService.findExcursions(LocalDate.of(2022,5,1),
                    LocalDate.of(2022,8,29), "Santiago de Compostela");
            assertEquals(2, foundExcursions3.size());
            assertEquals(foundExcursions3.get(0), excursion3);
            assertEquals(foundExcursions3.get(1), excursion4);

            List<Excursion> foundExcursions4 = excursionService.findExcursions(LocalDate.of(2022,11,1),
                    LocalDate.of(2022,12,31), "Vigo");
            assertEquals(0, foundExcursions4.size());
        }
        finally {
            for(Excursion excursion : excursionList){
                removeExcursion(excursion.getIdExcursion());
            }
        }
    }

    // CP-2: Encontrar excursiones especificando fecha límite inferior
    @Test
    public void testFindExcursionsWithInitDate() throws InputValidationException{

        List<Excursion> excursionList = new ArrayList<>();
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //Excursion 1
        Excursion excursion1 = createExcursion(new Excursion("A Coruña", "Excursion al Domus",
                LocalDateTime.parse("2022-07-20 09:00:00", formatterDateTime), 15, 75));
        excursionList.add(excursion1);
        //Excursion 2
        Excursion excursion2 = createExcursion(new Excursion("Vigo", "Paseo por Castrelos",
                LocalDateTime.parse("2022-07-24 18:15:00", formatterDateTime), 7.5f, 100));
        excursionList.add(excursion2);
        //Excursion 3
        Excursion excursion3 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la catedral",
                LocalDateTime.parse("2022-07-30 18:15:00", formatterDateTime), 10, 50));
        excursionList.add(excursion3);
        //Excursion 4
        Excursion excursion4 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la ciudad de la cultura",
                LocalDateTime.parse("2022-08-28 18:15:00", formatterDateTime), 10, 50));
        excursionList.add(excursion4);

        try{
            List<Excursion> foundExcursions1 = excursionService.findExcursions(LocalDate.of(2022, 7, 1),
                    null, "A Coruña");
            assertEquals(1, foundExcursions1.size());

            List<Excursion> foundExcursions2 = excursionService.findExcursions(LocalDate.of(2022,8,1),
                    null, "Santiago de Compostela");
            assertEquals(1, foundExcursions2.size());

            List<Excursion> foundExcursions3 = excursionService.findExcursions(LocalDate.of(2022,5,1),
                    null, "Santiago de Compostela");
            assertEquals(2, foundExcursions3.size());
            assertEquals(foundExcursions3.get(0), excursion3);
            assertEquals(foundExcursions3.get(1), excursion4);

            List<Excursion> foundExcursions4 = excursionService.findExcursions(LocalDate.of(2022,11,1),
                    null, "Vigo");
            assertEquals(0, foundExcursions4.size());
        }
        finally {
            for(Excursion excursion : excursionList){
                removeExcursion(excursion.getIdExcursion());
            }
        }
    }

    // CP-3: Encontrar excursiones especificando fecha límite superior
    @Test
    public void testFindExcursionsWithEndDate() throws InputValidationException{

        List<Excursion> excursionList = new ArrayList<>();
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //Excursion 1
        Excursion excursion1 = createExcursion(new Excursion("A Coruña", "Excursion al Domus",
                LocalDateTime.parse("2022-09-15 09:00:00", formatterDateTime), 15, 75));
        excursionList.add(excursion1);
        //Excursion 2
        Excursion excursion2 = createExcursion(new Excursion("Vigo", "Paseo por Castrelos",
                LocalDateTime.parse("2022-08-01 18:15:00", formatterDateTime), 7.5f, 100));
        excursionList.add(excursion2);
        //Excursion 3
        Excursion excursion3 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la catedral",
                LocalDateTime.parse("2022-07-27 18:15:00", formatterDateTime), 10, 50));
        excursionList.add(excursion3);
        //Excursion 4
        Excursion excursion4 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la ciudad de la cultura",
                LocalDateTime.parse("2022-08-28 18:15:00", formatterDateTime), 10, 50));
        excursionList.add(excursion4);

        try{
            List<Excursion> foundExcursions1 = excursionService.findExcursions(null,
                    LocalDate.of(2022,9,28), "A Coruña");
            assertEquals(1, foundExcursions1.size());

            List<Excursion> foundExcursions2 = excursionService.findExcursions(null,
                    LocalDate.of(2022,7,30), "Santiago de Compostela");
            assertEquals(1, foundExcursions2.size());

            List<Excursion> foundExcursions3 = excursionService.findExcursions(null,
                    LocalDate.of(2022,8,29), "Santiago de Compostela");
            assertEquals(2, foundExcursions3.size());
            assertEquals(foundExcursions3.get(0), excursion3);
            assertEquals(foundExcursions3.get(1), excursion4);

            List<Excursion> foundExcursions4 = excursionService.findExcursions(null,
                    LocalDate.of(2022,7,15), "Vigo");
            assertEquals(0, foundExcursions4.size());
        }
        finally {
            for(Excursion excursion : excursionList){
                removeExcursion(excursion.getIdExcursion());
            }
        }
    }

    // CP-4: Encontrar excursiones sin especificar fechas
    @Test
    public void testFindExcursionsWithoutDates() throws InputValidationException{

        List<Excursion> excursionList = new ArrayList<>();
        DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //Excursion 1
        Excursion excursion1 = createExcursion(new Excursion("A Coruña", "Excursion al Domus",
                LocalDateTime.parse("2022-09-18 09:00:00", formatterDateTime), 15, 75));
        excursionList.add(excursion1);
        //Excursion 2
        Excursion excursion2 = createExcursion(new Excursion("Vigo", "Paseo por Castrelos",
                LocalDateTime.parse("2022-10-22 18:15:00", formatterDateTime), 7.5f, 100));
        excursionList.add(excursion2);
        //Excursion 3
        Excursion excursion3 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la catedral",
                LocalDateTime.parse("2022-11-01 18:15:00", formatterDateTime), 10, 50));
        excursionList.add(excursion3);
        //Excursion 4
        Excursion excursion4 = createExcursion(new Excursion("Santiago de Compostela", "Visita a la ciudad de la cultura",
                LocalDateTime.parse("2022-12-24 08:30:00", formatterDateTime), 10, 55));
        excursionList.add(excursion4);
        //Excursion 5
        Excursion excursion5 = createExcursion(new Excursion("Santiago de Compostela", "Paseo por el Monte do Gozo",
                LocalDateTime.parse("2022-07-20 15:30:00", formatterDateTime), 5, 100));
        excursionList.add(excursion5);
        //Excursion 6
        Excursion excursion6 = createExcursion(new Excursion("A Coruña", "Excursion al planetario",
                LocalDateTime.parse("2022-08-17 09:00:00", formatterDateTime), 7.5f, 25));
        excursionList.add(excursion6);
        //Excursion 7
        Excursion excursion7 = createExcursion(new Excursion("Lugo", "Visita a la muralla",
                LocalDateTime.parse("2022-07-19 18:30:00", formatterDateTime), 10, 55));
        excursionList.add(excursion7);

        try {
            excursion7.setStartDateTime(LocalDateTime.now().plusHours(23).withNano(0));
            updateExcursion(excursion7);

            List<Excursion> foundExcursions1 = excursionService.findExcursions(null, null, "A Coruña");
            assertEquals(2, foundExcursions1.size());
            assertEquals(excursion6, foundExcursions1.get(0));
            assertEquals(excursion1, foundExcursions1.get(1));
            List<Excursion> foundExcursions2 = excursionService.findExcursions(null, null, "Santiago de Compostela");
            assertEquals(3, foundExcursions2.size());
            assertEquals(excursion5, foundExcursions2.get(0));
            assertEquals(excursion3, foundExcursions2.get(1));
            assertEquals(excursion4, foundExcursions2.get(2));
            List<Excursion> foundExcursions3 = excursionService.findExcursions(null, null, "Vigo");
            assertEquals(1, foundExcursions3.size());
            List<Excursion> foundExcursions4 = excursionService.findExcursions(null, null, "Ourense");
            assertEquals(0, foundExcursions4.size());
            List<Excursion> foundExcursions5 = excursionService.findExcursions(null, null, "Lugo");
            assertEquals(0, foundExcursions5.size());
        }
        finally {
            for(Excursion excursion : excursionList){
                removeExcursion(excursion.getIdExcursion());
            }
        }
    }

    // CP-5: Intentar encontrar excursiones indicando mal los datos de búsqueda
    @Test
    public void testInvalidFindExcursions() {

        assertThrows(InputValidationException.class, () ->
                excursionService.findExcursions(null, null, ""));

        assertThrows(InputValidationException.class, () ->
                excursionService.findExcursions(null, null, null));

        assertThrows(InputValidationException.class, () ->
                excursionService.findExcursions(null, LocalDate.now(), "A Coruña"));

        assertThrows(InputValidationException.class, () ->
                excursionService.findExcursions(LocalDate.now().plusDays(10), LocalDate.now().plusDays(9), "A Coruña"));
    }

    // Caso de uso 4 - Lucas
    // reserva
    // CP-1: Realizar una reserva correctamente
    @Test
    public void testAddReserva() throws InputValidationException, InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException {

        Excursion excursion = createExcursion(getValidExcursion());
        Reserva reserva = null;

        try {
            reserva = excursionService.reserva(userEmail, excursion.getIdExcursion(), numCreditCard, 1);
            Reserva foundReserva = findReserva(reserva.getIdReserva());
            Excursion foundExcursion = findExcursion(excursion.getIdExcursion());

            assertEquals(reserva, foundReserva);
            assertEquals(foundExcursion.getFreePlaces(), foundExcursion.getMaxPlaces()-1);
        }
        finally {
            if (reserva != null) {
                removeReserva(reserva.getIdReserva());
            }
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-2: Realizar una reserva con una tarjeta de crédito inválida
    @Test
    public void testAddReservaWithInvalidCreditCard() {

        Excursion excursion = createExcursion(getValidExcursion());

        try {

            //Numero de tarjeta incorrecto
            assertThrows(InputValidationException.class, () -> {
                Reserva reserva = excursionService.reserva(userEmail, excursion.getIdExcursion(), "tarjeta",
                        3);
                removeReserva(reserva.getIdReserva());
            });

            //Numero de tarjeta nulo
            assertThrows(InputValidationException.class, () -> {
                Reserva reserva = excursionService.reserva(userEmail, excursion.getIdExcursion(), "",
                        3);
                removeReserva(reserva.getIdReserva());
            });
        }
        finally {
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-3: Realizar una reserva para una excursion inexistente
    @Test
    public void testAddReservaButExcursionNotExists() {
        assertThrows(InstanceNotFoundException.class, () -> {
            Reserva reserva = excursionService.reserva(userEmail, NON_EXISTENT_EXC_ID, numCreditCard, 3);
            removeReserva(reserva.getIdReserva());
        });
    }

    // CP-4: Realizar dos reservas con el mismo userEmail
    @Test
    public void testAddReservaRepeatedly() throws InputValidationException, InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException {

        Excursion excursion = createExcursion(getValidExcursion());
        Reserva reserva1 = null;
        Reserva reserva2 = null;

        try{
            reserva1 = excursionService.reserva(userEmail, excursion.getIdExcursion(), numCreditCard, 3);
            Reserva foundReserva1 = findReserva(reserva1.getIdReserva());
            Excursion foundExcursion1 = findExcursion(excursion.getIdExcursion());
            assertEquals(reserva1, foundReserva1);
            assertEquals(foundExcursion1.getFreePlaces(), foundExcursion1.getMaxPlaces()-3);

            reserva2 = excursionService.reserva(userEmail, excursion.getIdExcursion(), numCreditCard, 2);
            Reserva foundReserva2 = findReserva(reserva2.getIdReserva());
            Excursion foundExcursion2 = findExcursion(excursion.getIdExcursion());
            assertEquals(reserva2, foundReserva2);
            assertEquals(foundExcursion2.getFreePlaces(), foundExcursion2.getMaxPlaces()-5);
        }
        finally {
            if(reserva1 != null){
                removeReserva(reserva1.getIdReserva());
            }
            if(reserva2 != null){
                removeReserva(reserva2.getIdReserva());
            }
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-5: Realizar una reserva con un email inválido
    @Test
    public void testAddReservaWithInvalidEmail() {

        Excursion excursion = createExcursion(getValidExcursion());

        try {

            //email incorrecto
            assertThrows(InputValidationException.class, () -> {
                Reserva reserva = excursionService.reserva("este es un email", excursion.getIdExcursion(),
                        numCreditCard, 3);
                removeReserva(reserva.getIdReserva());
            });

            //email nulo
            assertThrows(InputValidationException.class, () -> {
                Reserva reserva = excursionService.reserva("", excursion.getIdExcursion(),
                        numCreditCard, 3);
                removeReserva(reserva.getIdReserva());
            });
        }
        finally {
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-6: Realizar reserva cuando la excursion ya está completa
    @Test
    public void testAddReservaWhenExcursionFull() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Excursion excursion = createExcursion(new Excursion("A Coruña", "Excursion a la ciudad",
                LocalDateTime.parse("2022-07-25 18:30:00", formatter), 15, 1));

        try {
            assertThrows(MaxParticipantsException.class, () -> {
                Reserva reserva = excursionService.reserva(userEmail, excursion.getIdExcursion(), numCreditCard, 3);
                excursionService.reserva(userEmail, excursion.getIdExcursion(), numCreditCard, 3);
                removeReserva(reserva.getIdReserva());
            });
        }
        finally {
            removeExcursion(excursion.getIdExcursion());
        }
    }

    // CP-7: Realizar reserva para una excursión que empieza en menos de 24 horas
    @Test
    public void testAddReservaWhenStartIn24H() {

        Excursion excursion = createExcursion(new Excursion("Barcelona", "Visita a la Sagrada Familia",
                LocalDateTime.now().withNano(0).plusHours(72).plusSeconds(5), 10, 100));
        Long idExcursion = excursion.getIdExcursion();

        try {
            excursion.setStartDateTime(excursion.getStartDateTime().withNano(0).minusHours(49));
            updateExcursion(excursion);

            assertThrows(LateRegisterException.class, () ->{
                Reserva reserva = excursionService.reserva(userEmail, idExcursion, numCreditCard, 1);
                removeReserva(reserva.getIdReserva());
            });
        }
        finally {
            removeExcursion(idExcursion);
        }
    }

    // CP-8: Intentar hacer una reserva de más de 5 plazas
    @Test
    public void testAddReservaWithInvalidPlaces() {

        Excursion excursion = createExcursion(getValidExcursion());

        assertThrows(InputValidationException.class, () -> {
            Reserva reserva = excursionService.reserva(userEmail, excursion.getIdExcursion(), numCreditCard, 8);
            removeReserva(reserva.getIdReserva());
        });

        removeExcursion(excursion.getIdExcursion());
    }

    // Caso de uso 5 - Jose
    // cancelReserva
    // CP-1: Cancelar reserva correctamente
    @Test
    public void testValidCancelReserva() throws InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException, InputValidationException, InvalidUserException,
            AlreadyCanceledException, LateCancelException {

        Excursion excursion = createExcursion(getValidExcursion());
        Long idExcursion = excursion.getIdExcursion();
        Long idReserva = null;

        try {
            Reserva reserva = excursionService.reserva(userEmail, idExcursion, numCreditCard, 1);
            idReserva = reserva.getIdReserva();
            Reserva canceledReserva = excursionService.cancelReserva(idReserva, userEmail);
            Excursion foundExcursion = findExcursion(idExcursion);

            assertNotEquals(reserva, canceledReserva);
            assertNull(reserva.getCancelDateTime());
            assertTrue(canceledReserva.getCancelDateTime().isBefore(LocalDateTime.now().plusSeconds(3)));
            assertTrue(canceledReserva.getCancelDateTime().isAfter(LocalDateTime.now().minusSeconds(3)));
            reserva.setCancelDateTime(canceledReserva.getCancelDateTime());
            assertEquals(reserva, canceledReserva);
            assertEquals(foundExcursion.getMaxPlaces(), foundExcursion.getFreePlaces());
        }
        finally {
            if (idReserva != null) {
                removeReserva(idReserva);
            }
            removeExcursion(idExcursion);
        }
    }

    // CP-2: Intentamos cancelar reserva cuando quedan menos de 48 horas para que empiece la excursion
    @Test
    public void testLateCancelReserva() throws InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException, InputValidationException {

        Excursion excursion = createExcursion(new Excursion("Barcelona", "Visita a la Sagrada Familia",
                LocalDateTime.now().withNano(0).plusHours(72).plusSeconds(5), 10, 100));
        Long idExcursion = excursion.getIdExcursion();
        Long idReserva = null;

        try {
            excursion.setStartDateTime(excursion.getStartDateTime().withNano(0).minusHours(25));
            updateExcursion(excursion);

            Reserva reserva = excursionService.reserva(userEmail, idExcursion, numCreditCard, 1);
            idReserva = reserva.getIdReserva();

            assertThrows(LateCancelException.class, () ->
                    excursionService.cancelReserva(reserva.getIdReserva(), userEmail));
        }
        finally {
            if (idReserva != null) {
                removeReserva(idReserva);
            }
            removeExcursion(idExcursion);
        }
    }

    // CP-3: Intentamos cancelar una reserva que ya hemos cancelado previamente
    @Test
    public void testCancelReservaAlreadyCanceled() throws InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException, InputValidationException,
            InvalidUserException, AlreadyCanceledException, LateCancelException {

        Excursion excursion = createExcursion(getValidExcursion());
        Long idExcursion = excursion.getIdExcursion();

        Long idReserva = null;

        try {
            Reserva reserva = excursionService.reserva(userEmail, idExcursion, numCreditCard, 1);
            idReserva = reserva.getIdReserva();
            excursionService.cancelReserva(reserva.getIdReserva(), userEmail);

            assertThrows(AlreadyCanceledException.class, () ->
                    excursionService.cancelReserva(reserva.getIdReserva(), userEmail));
        }
        finally {
            if (idReserva != null) {
                removeReserva(idReserva);
            }
            removeExcursion(idExcursion);
        }
    }

    // CP-4: tratamos de cancelar una reserva inexistente
    @Test
    public void testCancelNonExistentReserva() throws InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException, InputValidationException {

        Excursion excursion = createExcursion(getValidExcursion());
        Long idExcursion = excursion.getIdExcursion();
        Long idReserva = null;

        try {
            Reserva reserva = excursionService.reserva(userEmail, idExcursion, numCreditCard, 1);
            idReserva = reserva.getIdReserva();

            assertThrows(InstanceNotFoundException.class, () ->
                    excursionService.cancelReserva(NON_EXISTENT_RES_ID, userEmail));
        }
        finally {
            if (idReserva != null) {
                removeReserva(idReserva);
            }
            removeExcursion(idExcursion);
        }
    }

    // CP-5: no se especifica correctamente el email al querer cancelar una reserva
    @Test
    public void testCancelReservaInvalidInput() throws InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException, InputValidationException {

        Excursion excursion = createExcursion(getValidExcursion());
        Long idExcursion = excursion.getIdExcursion();
        Long idReserva = null;

        try {
            Reserva reserva = excursionService.reserva(userEmail, idExcursion, numCreditCard, 1);
            idReserva = reserva.getIdReserva();

            assertThrows(InputValidationException.class, () ->
                    excursionService.cancelReserva(reserva.getIdReserva(), null));
            assertThrows(InputValidationException.class, () ->
                    excursionService.cancelReserva(reserva.getIdReserva(), ""));
            assertThrows(InputValidationException.class, () ->
                    excursionService.cancelReserva(reserva.getIdReserva(), "fulanito"));
        }
        finally {
            if (idReserva != null) {
                removeReserva(idReserva);
            }
            removeExcursion(idExcursion);
        }
    }

    // CP-6: el email especificado para cancelar la reserva no coincide con el que figura en ella
    @Test
    public void testCancelReservaInvalidUser() throws InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException, InputValidationException {

        Excursion excursion = createExcursion(getValidExcursion());
        Long idExcursion = excursion.getIdExcursion();
        Long idReserva = null;

        try {
            Reserva reserva = excursionService.reserva(userEmail, idExcursion, numCreditCard, 1);
            idReserva = reserva.getIdReserva();

            assertThrows(InvalidUserException.class, () ->
                    excursionService.cancelReserva(reserva.getIdReserva(), "jose@gmail.com"));
        }
        finally {
            if (idReserva != null) {
                removeReserva(idReserva);
            }
            removeExcursion(idExcursion);
        }
    }

    // Caso de uso 6 - Elena
    // findUserReservas
    // CP-1: Encontrar las reservas de un usuario
    @Test
    public void testValidFindUserReservas() throws InstanceNotFoundException, InputValidationException,
            MaxParticipantsException, LateRegisterException {

        Excursion excursion1 = createExcursion(getValidExcursion());
        Excursion excursion2 = createExcursion(getValidExcursion());
        Excursion excursion3 = createExcursion(getValidExcursion());

        Reserva reserva1 = null;
        Reserva reserva2 = null;
        Reserva reserva3 = null;

        try {
            reserva1 = excursionService.reserva(userEmail, excursion1.getIdExcursion(), numCreditCard, 1);
            reserva2 = excursionService.reserva(userEmail, excursion2.getIdExcursion(), numCreditCard, 1);
            reserva3 = excursionService.reserva(userEmail, excursion3.getIdExcursion(), numCreditCard, 1);
            List<Reserva> listaReservasBuscada = excursionService.findUserReservas(userEmail);
            assertEquals(reserva1, listaReservasBuscada.get(2));
            assertEquals(reserva2, listaReservasBuscada.get(1));
            assertEquals(reserva3, listaReservasBuscada.get(0));

            assertEquals(3, listaReservasBuscada.size());
        }
        finally {
            if(reserva1 != null){
                removeReserva(reserva1.getIdReserva());
            }
            if(reserva2 != null){
                removeReserva(reserva2.getIdReserva());
            }
            if(reserva3 != null){
                removeReserva(reserva3.getIdReserva());
            }
            removeExcursion(excursion1.getIdExcursion());
            removeExcursion(excursion2.getIdExcursion());
            removeExcursion(excursion3.getIdExcursion());
        }
    }

    // CP-2: Intentar encontrar reservas de un usuario inválido
    @Test
    public void testFindUserReservasInvalidUser() {

        assertThrows(InputValidationException.class, () ->
                excursionService.findUserReservas(""));

        assertThrows(InputValidationException.class, () ->
                excursionService.findUserReservas(null));

        assertThrows(InputValidationException.class, () ->
                excursionService.findUserReservas("hola"));
    }

    // CP-3: Buscar reservas inexistentes
    @Test
    public void testFindNoUserReservas() throws InstanceNotFoundException, LateRegisterException,
            MaxParticipantsException, InputValidationException {

        Excursion excursion = createExcursion(getValidExcursion());
        Reserva reserva = null;

        try {
            reserva = excursionService.reserva(userEmail, excursion.getIdExcursion(), numCreditCard, 1);
            List<Reserva> listaReservasBuscada = excursionService.findUserReservas("jose@gmail.com");

            assertEquals(0, listaReservasBuscada.size());
        }
        finally {
            if(reserva != null){
                removeReserva(reserva.getIdReserva());
            }
            removeExcursion(excursion.getIdExcursion());
        }
    }
}