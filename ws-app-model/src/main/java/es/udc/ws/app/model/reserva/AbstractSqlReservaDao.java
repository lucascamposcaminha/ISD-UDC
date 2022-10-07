package es.udc.ws.app.model.reserva;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlReservaDao implements SqlReservaDao{

    @Override
    public Reserva find(Connection connection, Long idReserva) throws InstanceNotFoundException{

        String queryString = "SELECT idExcursion, userEmail, numPlaces, numCreditCard, registerDateTime," +
                " cancelDateTime, price FROM Reserva WHERE idReserva = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;

            preparedStatement.setLong(i, idReserva);
            ResultSet resultSet =  preparedStatement.executeQuery();
            if(!resultSet.next()){
                throw new InstanceNotFoundException(idReserva, Reserva.class.getName());
            }

            Long idExcursion = resultSet.getLong(i++);
            String userEmail = resultSet.getString(i++);
            int numPlaces = resultSet.getInt(i++);
            String numCreditCard = resultSet.getString(i++);
            Timestamp registerDateTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime registerDateTime = registerDateTimestamp.toLocalDateTime().withNano(0);
            Timestamp cancelDateTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime cancelDateTime = null;
            if(cancelDateTimestamp != null) {
                cancelDateTime = cancelDateTimestamp.toLocalDateTime().withNano(0);
            }
            float price = resultSet.getFloat(i);

            return new Reserva(idReserva, idExcursion, userEmail, numPlaces, numCreditCard, registerDateTime,
                    cancelDateTime, price);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> findReservas(Connection connection, String email){

        String queryString = "SELECT idReserva, idExcursion, userEmail, numPlaces, numCreditCard, registerDateTime," +
                " cancelDateTime, price FROM Reserva WHERE userEmail = ? ORDER BY registerDateTime DESC, idReserva DESC";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;
            preparedStatement.setString(i, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Reserva> reservaList = new ArrayList<>();
            Reserva newReserva;

            while (resultSet.next()){

                i = 1;

                Long idReserva = resultSet.getLong(i++);
                Long idExcursion = resultSet.getLong(i++);
                String userEmail = resultSet.getString(i++);
                int numPlaces = resultSet.getInt(i++);
                String numCreditCard = resultSet.getString(i++);
                Timestamp registerDateTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime registerDateTime = registerDateTimestamp.toLocalDateTime();
                Timestamp cancelDateTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime cancelDateTime = null;
                if(cancelDateTimestamp != null){
                    cancelDateTime = cancelDateTimestamp.toLocalDateTime();
                }
                float price = resultSet.getFloat(i);

                newReserva = new Reserva(idReserva, idExcursion, userEmail, numPlaces, numCreditCard,
                        registerDateTime, cancelDateTime, price);
                reservaList.add(newReserva);
            }
            return reservaList;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long idReserva) throws InstanceNotFoundException{

        String queryString = "DELETE FROM Reserva WHERE idReserva = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            preparedStatement.setLong(1, idReserva);

            int removedRows = preparedStatement.executeUpdate();

            if(removedRows == 0){
                throw new InstanceNotFoundException(idReserva, Reserva.class.getName());
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Reserva reserva) throws InstanceNotFoundException{

        String queryString = "UPDATE Reserva SET idExcursion = ?, userEmail = ?, numPlaces = ?, numCreditCard = ?," +
                " cancelDateTime = ?, price = ? WHERE idReserva = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, reserva.getIdExcursion());
            preparedStatement.setString(i++, reserva.getUserEmail());
            preparedStatement.setInt(i++, reserva.getNumPlaces());
            preparedStatement.setString(i++, reserva.getNumCreditCard());
            Timestamp cancelDateTimestamp = null;
            if (reserva.getCancelDateTime() != null) {
                cancelDateTimestamp = Timestamp.valueOf(reserva.getCancelDateTime());
            }
            preparedStatement.setTimestamp(i++, cancelDateTimestamp);
            preparedStatement.setFloat(i++, reserva.getPrice());
            preparedStatement.setLong(i, reserva.getIdReserva());

            int updateRows = preparedStatement.executeUpdate();

            if (updateRows == 0) {
                throw new InstanceNotFoundException(reserva.getIdExcursion(), Reserva.class.getName());
            }

        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
