package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientExcursionDto {

    private Long idExcursion;
    private String city;
    private String description;
    private LocalDateTime startDateTime;
    private float price;
    private int maxPlaces;
    private int occupiedPlaces;

    // Este lo usamos para devolver excursiones
    public ClientExcursionDto(Long idExcursion, String city, String description, LocalDateTime startDateTime,
                              float price, int maxPlaces, int occupiedPlaces) {
        this.idExcursion = idExcursion;
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
    }

    // Este para crear excursiones
    public ClientExcursionDto(String city, String description, LocalDateTime startDateTime,
                              float price, int maxPlaces) {
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.maxPlaces = maxPlaces;
    }

    // Este para actualizar excursiones
    public ClientExcursionDto(Long idExcursion, String city, String description, LocalDateTime startDateTime,
                              float price, int maxPlaces) {
        this.idExcursion = idExcursion;
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.maxPlaces = maxPlaces;
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

    public int getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(int occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    @Override
    public String toString(){
        return "ExcursionD {idExcursion= " + idExcursion +
                ", city= " + city +
                ", description= " + description +
                ", startDateTime= " + startDateTime +
                ", price= " + price +
                ", maxPlaces= " + maxPlaces +
                ", occupiedPlaces= " + occupiedPlaces + "}";
    }
}
