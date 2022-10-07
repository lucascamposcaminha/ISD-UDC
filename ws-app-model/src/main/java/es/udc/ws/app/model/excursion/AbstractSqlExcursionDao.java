package es.udc.ws.app.model.excursion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlExcursionDao implements SqlExcursionDao{

    protected AbstractSqlExcursionDao() {}

    @Override
    public void update(Connection connection, Excursion updatedExcursion) throws InstanceNotFoundException {
        String queryString = "UPDATE Excursion SET city = ?, description = ?, startDateTime = ?," +
                " price = ?, maxPlaces = ?, freePlaces = ? WHERE idExcursion = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setString(i++, updatedExcursion.getCity());
            preparedStatement.setString(i++, updatedExcursion.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(updatedExcursion.getStartDateTime()));
            preparedStatement.setFloat(i++, updatedExcursion.getPrice());
            preparedStatement.setInt(i++, updatedExcursion.getMaxPlaces());
            preparedStatement.setInt(i++, updatedExcursion.getFreePlaces());
            preparedStatement.setLong(i, updatedExcursion.getIdExcursion());

            if (preparedStatement.executeUpdate() == 0) {
                throw new InstanceNotFoundException(updatedExcursion.getIdExcursion(), Excursion.class.getName());
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Excursion find(Connection connection, Long idExcursion) throws InstanceNotFoundException{

        String queryString = "SELECT city, description, startDateTime, registerDateTime, price, maxPlaces, freePlaces " +
                "FROM Excursion WHERE idExcursion = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;
            preparedStatement.setLong(i, idExcursion);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                throw new InstanceNotFoundException(idExcursion, Excursion.class.getName());
            }

            String city = resultSet.getString(i++);
            String description = resultSet.getString(i++);
            LocalDateTime startDateTime = resultSet.getTimestamp(i++).toLocalDateTime().withNano(0);
            LocalDateTime registerDateTime = resultSet.getTimestamp(i++).toLocalDateTime().withNano(0);
            float price = resultSet.getFloat(i++);
            int maxPlaces = resultSet.getInt(i++);
            int freePlaces = resultSet.getInt(i);

            return new Excursion(idExcursion, city, description, startDateTime, registerDateTime, price, maxPlaces,
                    freePlaces);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Excursion> findExcursions(Connection connection, String city, LocalDate ini, LocalDate end) {

        String queryString = "SELECT idExcursion, city, description, startDateTime, registerDateTime, price, maxPlaces," +
                "freePlaces FROM Excursion WHERE city = ? AND freePlaces > 0 AND startDateTime >= ?";

        Timestamp iniTimestamp;
        if (ini == null || ini.atStartOfDay().isBefore(LocalDateTime.now().withNano(0).plusHours(24))) {
            iniTimestamp = Timestamp.valueOf(LocalDateTime.now().withNano(0).plusHours(24));
        }
        else {
            iniTimestamp = Timestamp.valueOf(ini.atStartOfDay().plusSeconds(1));
        }

        Timestamp endTimeStamp = null;
        if (end != null) {
            queryString += " AND startDateTime <= ?";
            endTimeStamp = Timestamp.valueOf(end.atTime(LocalTime.MAX).withNano(0));
        }

        queryString += "ORDER BY startDateTime";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;
            preparedStatement.setString(i++, city);
            preparedStatement.setTimestamp(i++, iniTimestamp);
            if(end != null){
                preparedStatement.setTimestamp(i, endTimeStamp);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Excursion> excursions = new ArrayList<>();

            while (resultSet.next()){
                i = 1;
                Long idExcursion = resultSet.getLong(i++);
                String resultCity = resultSet.getString(i++);
                String description = resultSet.getString(i++);
                Timestamp startDateTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime startDateTime = startDateTimestamp.toLocalDateTime().withNano(0);
                Timestamp registerDateTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime registerDateTime = registerDateTimestamp.toLocalDateTime().withNano(0);
                float price = resultSet.getFloat(i++);
                int maxPlaces = resultSet.getInt(i++);
                int freePlaces = resultSet.getInt(i);

                excursions.add(new Excursion(idExcursion, resultCity, description, startDateTime, registerDateTime, price,
                        maxPlaces, freePlaces));
            }
            return excursions;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long idExcursion) throws InstanceNotFoundException{
        String queryString = "DELETE FROM Excursion WHERE idExcursion = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            preparedStatement.setLong(1, idExcursion);
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0){
                throw new InstanceNotFoundException(idExcursion, Excursion.class.getName());
            }

        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}