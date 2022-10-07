package es.udc.ws.app.model.excursion;

import java.sql.*;

public class Jdbc3CcSqlExcursionDao extends AbstractSqlExcursionDao{

    @Override
    public Excursion create(Connection connection, Excursion excursion){

        String queryString = "INSERT INTO Excursion (city, description, startDateTime, registerDateTime," +
                "price, maxPlaces, freePlaces) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){

            int i= 1;
            preparedStatement.setString(i++, excursion.getCity());
            preparedStatement.setString(i++, excursion.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getStartDateTime().withNano(0)));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getRegisterDateTime().withNano(0)));
            preparedStatement.setFloat(i++, excursion.getPrice());
            preparedStatement.setInt(i++, excursion.getMaxPlaces());
            preparedStatement.setInt(i, excursion.getFreePlaces());

            int modifiedRows = preparedStatement.executeUpdate();

            if(modifiedRows == 0){
                throw new SQLException("No rows were added");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()){
                throw new SQLException("JDBC driver did not return generated key");
            }
            Long idExcursion = resultSet.getLong(1);

            return new Excursion(idExcursion, excursion.getCity(), excursion.getDescription(), excursion.getStartDateTime().withNano(0),
                    excursion.getRegisterDateTime().withNano(0), excursion.getPrice(), excursion.getMaxPlaces(), excursion.getFreePlaces());
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
