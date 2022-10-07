package es.udc.ws.app.model.excursion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface SqlExcursionDao {

    Excursion create(Connection connection, Excursion excursion);

    Excursion find(Connection connection, Long idExcursion) throws InstanceNotFoundException;

    List<Excursion> findExcursions(Connection connection, String city, LocalDate ini, LocalDate end);

    void remove(Connection connection, Long idExcursion) throws InstanceNotFoundException;
    
    void update(Connection connection, Excursion updatedExcursion) throws InstanceNotFoundException;

}
