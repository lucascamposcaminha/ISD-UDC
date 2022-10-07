package es.udc.ws.app.model.reserva;

import java.sql.*;

public class Jdbc3CcSqlReservaDao extends AbstractSqlReservaDao{

    @Override
    public Reserva create(Connection connection, Reserva reserva){

        String queryString = "INSERT INTO Reserva (idExcursion, userEmail, numPlaces, numCreditCard, " +
                "registerDateTime, price) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){

            int i = 1;
            preparedStatement.setLong(i++, reserva.getIdExcursion());
            preparedStatement.setString(i++, reserva.getUserEmail());
            preparedStatement.setInt(i++, reserva.getNumPlaces());
            preparedStatement.setString(i++, reserva.getNumCreditCard());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(reserva.getRegisterDateTime()));
            preparedStatement.setFloat(i, reserva.getPrice());

            int modifiedRows = preparedStatement.executeUpdate();

            if(modifiedRows == 0){
                throw new SQLException("No rows were added");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()){
                throw new SQLException("JDBC driver did not return generated key");
            }
            Long idReserva = resultSet.getLong(1);

            return new Reserva(idReserva, reserva.getIdExcursion(), reserva.getUserEmail(), reserva.getNumPlaces(), reserva.getNumCreditCard(),
                    reserva.getRegisterDateTime(), null, reserva.getPrice());
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
