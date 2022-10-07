package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestExcursionDto {

    private Long idExcursion;
    private String city;
    private String description;
    private LocalDateTime startDateTime;
    private float price;
    private int maxPlaces;
    private int freePlaces;

    public RestExcursionDto() {}

    public RestExcursionDto(Long idExcursion, String city, String description, LocalDateTime startDateTime,
                            float price, int maxPlaces, int freePlaces) {
        this.idExcursion = idExcursion;
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.maxPlaces = maxPlaces;
        this.freePlaces = freePlaces;
    }

    public RestExcursionDto(Long idExcursion, String city, String description, LocalDateTime startDateTime, float price, int maxPlaces) {
        this.idExcursion = idExcursion;
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
    public String toString() {
        return "RestExcursionDto[" +
                "idExcursion= " + idExcursion +
                ", city= " + city +
                ", description= " + description +
                ", startDateTime= " + startDateTime +
                ", price= " + price +
                ", maxPlaces= " + maxPlaces +
                ", freePlaces= " + freePlaces + ']';
    }
}
