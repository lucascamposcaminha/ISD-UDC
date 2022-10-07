package es.udc.ws.app.model.excursion;

import java.time.LocalDateTime;
import java.util.Objects;

public class Excursion {
    private Long idExcursion;
    private String city;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime registerDateTime;
    private float price;
    private int maxPlaces;
    private int freePlaces;

    // Este constructor lo usamos para devolver excursiones que obtenemos de la BD
    public Excursion(Long idExcursion, String city, String description, LocalDateTime startDateTime,
                     LocalDateTime registerDateTime, float price, int maxPlaces, int freePlaces) {
        this.idExcursion = idExcursion;
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.registerDateTime = registerDateTime;
        this.price = price;
        this.maxPlaces = maxPlaces;
        this.freePlaces = freePlaces;
    }

    // Este lo usamos para actualizar excursiones, es decir, sería el argumento updatedExcursion de updateExcursion (aunque si usamos el de arriba no pasaría nada)
    public Excursion(Long idExcursion, String city, String description, LocalDateTime startDateTime, float price, int maxPlaces) {
        this.idExcursion = idExcursion;
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.maxPlaces = maxPlaces;
    }

    // Este lo usamos como argumento para crear excursiones
    public Excursion(String city, String description, LocalDateTime startDateTime, float price, int maxPlaces) {
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.maxPlaces = maxPlaces;
        this.freePlaces = maxPlaces;
    }


    public Long getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(Long idExcursion) {
        this.idExcursion = idExcursion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(int maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Excursion excursion = (Excursion)obj;

        return Objects.equals(idExcursion, excursion.idExcursion) && Objects.equals(city, excursion.city) &&
                Objects.equals(description, excursion.description) && Objects.equals(startDateTime, excursion.startDateTime) &&
                price == excursion.price && maxPlaces == excursion.maxPlaces && freePlaces == excursion.freePlaces &&
                Objects.equals(registerDateTime, excursion.registerDateTime);
    }

    @Override
    public int hashCode(){
        return Objects.hash(idExcursion,city,description,startDateTime,registerDateTime,price,maxPlaces,freePlaces);
    }
}