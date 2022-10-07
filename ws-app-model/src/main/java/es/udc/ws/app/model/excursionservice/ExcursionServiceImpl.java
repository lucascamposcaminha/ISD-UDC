package es.udc.ws.app.model.excursionservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class ExcursionServiceImpl implements ExcursionService {

    private final DataSource dataSource;
    private SqlExcursionDao excursionDao = null;
    private SqlReservaDao reservaDao = null;

    public ExcursionServiceImpl(){
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        excursionDao = SqlExcursionDaoFactory.getDao();
        reservaDao = SqlReservaDaoFactory.getDao();
    }

    private void validateExcursion(Excursion excursion) throws InputValidationException {
        PropertyValidator.validateMandatoryString("city", excursion.getCity());
        PropertyValidator.validateMandatoryString("description", excursion.getDescription());
        PropertyValidator.validateDouble("price", excursion.getPrice(), 0, MAX_PRICE);
        PropertyValidator.validateDouble("maxPlaces", excursion.getMaxPlaces(), 1, MAX_PLACES);
    }

    private void validateEmail(String userEmail) throws InputValidationException{
        if(userEmail != null && userEmail.contains("@")){
            if(userEmail.indexOf("@") == 0 || userEmail.indexOf("@") == userEmail.length()){
                throw new InputValidationException("Email no valido");
            }
        }
        else{
            throw new InputValidationException("Email no valido");
        }
    }

    // Caso de uso 1 - Lucas
    @Override
    public Excursion addExcursion(Excursion excursion) throws InputValidationException {

        validateExcursion(excursion);

        if(excursion.getStartDateTime().isBefore(LocalDateTime.now().withNano(0).plusHours(72))){
            throw new InputValidationException("La fecha de celebración no puede ser inferior a dentro de 72 horas");
        }

        excursion.setRegisterDateTime(LocalDateTime.now().withNano(0));
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Excursion createdExcursion = excursionDao.create(connection, excursion);

                connection.commit();
                return createdExcursion;
            }
            catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
            catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 2 - Jose
    @Override
    public void updateExcursion(Excursion updatedExcursion)
            throws InputValidationException, InstanceNotFoundException, MinimumPlacesException,
            InvalidDateException, LateUpdateException {

        validateExcursion(updatedExcursion);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Excursion excursion = excursionDao.find(connection, updatedExcursion.getIdExcursion());
                if (excursion.getStartDateTime().isBefore(LocalDateTime.now().withNano(0).plusHours(72))){
                    throw new LateUpdateException("No se puede actualizar la excursión si quedan menos de 72 horas para que empiece");
                }
                if (updatedExcursion.getStartDateTime().isBefore(excursion.getStartDateTime())) {
                    throw new InvalidDateException("No se puede adelantar la fecha de celebración");
                }
                int reservedPlaces = excursion.getMaxPlaces() - excursion.getFreePlaces();
                if (updatedExcursion.getMaxPlaces() < reservedPlaces) {
                    throw new MinimumPlacesException("No puede haber menos plazas que las ya reservadas");
                }

                updatedExcursion.setFreePlaces(updatedExcursion.getMaxPlaces() - reservedPlaces);

                excursionDao.update(connection, updatedExcursion);
                connection.commit();

            } catch (InstanceNotFoundException | LateUpdateException | InvalidDateException |
                    MinimumPlacesException e) {
                connection.commit();
                throw e;
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

    // Caso de uso 3 - Elena
    @Override
    public List<Excursion> findExcursions(LocalDate ini, LocalDate end, String city)
            throws InputValidationException {

        PropertyValidator.validateMandatoryString("city", city);

        if (end != null) {
            if(end.isBefore(LocalDate.now().plusDays(1))){
                throw new InputValidationException("Las fechas especificadas no son válidas");
            }
            if (ini != null && end.isBefore(ini)) {
                throw new InputValidationException("Las fechas especificadas no son válidas");
            }
        }

        try (Connection connection = dataSource.getConnection()) {

            return excursionDao.findExcursions(connection, city, ini, end);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 4 - Lucas
    @Override
    public Reserva reserva(String userEmail, Long idExcursion, String numCreditCard, int numPlaces) throws InstanceNotFoundException,
            InputValidationException, MaxParticipantsException, LateRegisterException {

        validateEmail(userEmail);
        PropertyValidator.validateCreditCard(numCreditCard);
        PropertyValidator.validateDouble("numPlazas", numPlaces, 1, 5);
        String creditCard = numCreditCard.replaceAll("\\s+", "");

        if (numPlaces > 5) {
            throw new InputValidationException("No se pueden hacer reservas de más de 5 plazas");
        }

        try(Connection connection = dataSource.getConnection()){

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                Excursion excursion = excursionDao.find(connection, idExcursion);

                if(LocalDateTime.now().isAfter(excursion.getStartDateTime().withNano(0).minusHours(24))){
                    throw new LateRegisterException("Las reservas se cierran 24 horas antes del inicio de la excursion");
                }
                if(excursion.getFreePlaces() < numPlaces){
                    throw new MaxParticipantsException("No quedan plazas suficientes para esta reserva");
                }

                // Se guarda el precio de cada plaza, no el total
                Reserva newReserva = new Reserva(idExcursion, userEmail, numPlaces, creditCard,
                        LocalDateTime.now().withNano(0), excursion.getPrice());
                Reserva createdReserva = reservaDao.create(connection, newReserva);

                int newFreePlaces = excursion.getFreePlaces() - numPlaces;
                excursion.setFreePlaces(newFreePlaces);
                excursionDao.update(connection, excursion);
                connection.commit();
                return createdReserva;
            }
            catch (InstanceNotFoundException | LateRegisterException | MaxParticipantsException e){
                connection.commit();
                throw e;
            }
            catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
            catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 5 - Jose
    @Override
    public Reserva cancelReserva(Long idReserva, String userEmail) throws InstanceNotFoundException,
            InputValidationException, LateCancelException, InvalidUserException, AlreadyCanceledException {

        validateEmail(userEmail);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Reserva reserva = reservaDao.find(connection, idReserva);
                if (!userEmail.equals(reserva.getUserEmail())) {
                    throw new InvalidUserException("El email introducido no coincide " +
                            "con el que figura en la reserva");
                }
                if (reserva.getCancelDateTime() != null) {
                    throw new AlreadyCanceledException("La reserva ya se ha cancelado previamente");
                }

                Excursion excursion = excursionDao.find(connection, reserva.getIdExcursion());
                if (excursion.getStartDateTime().isBefore(LocalDateTime.now().withNano(0).plusHours(48))) {
                    throw new LateCancelException("No se puede cancelar una reserva en las 48 horas " +
                            "anteriores al inicio de la excursión");
                }

                reserva.setCancelDateTime(LocalDateTime.now().withNano(0));
                reservaDao.update(connection, reserva);
                excursion.setFreePlaces(excursion.getFreePlaces() + reserva.getNumPlaces());
                excursionDao.update(connection, excursion);

                connection.commit();
                return reserva;

            }
            catch (InstanceNotFoundException | InvalidUserException | LateCancelException |
                    AlreadyCanceledException e) {
                connection.commit();
                throw e;
            }
            catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Caso de uso 6 - Elena
    @Override
    public List<Reserva> findUserReservas(String userEmail) throws InputValidationException {

        validateEmail(userEmail);

        try (Connection connection = dataSource.getConnection()) {

            return reservaDao.findReservas(connection, userEmail);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
