package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestReservaDto {

    private Long idReserva;
    private LocalDateTime registerDateTime;
    private String userEmail;
    private int numPlaces;
    private String numCreditCard;
    private Long idExcursion;
    private LocalDateTime cancelDateTime;
    private float price;

    public RestReservaDto() {}

    public RestReservaDto(Long idReserva, LocalDateTime resDateTime, String userEmail, int numPlaces,
                          String numCreditCard, Long idExcursion, LocalDateTime cancelDateTime, float price) {
        this.idReserva = idReserva;
        this.registerDateTime = resDateTime;
        this.userEmail = userEmail;
        this.numPlaces = numPlaces;
        this.numCreditCard = numCreditCard;
        this.idExcursion = idExcursion;
        this.cancelDateTime = cancelDateTime;
        this.price = price;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }

    public String getNumCreditCard() {
        return numCreditCard;
    }

    public void setNumCreditCard(String numCreditCard) {
        this.numCreditCard = numCreditCard;
    }

    public Long getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(Long idExcursion) {
        this.idExcursion = idExcursion;
    }

    public LocalDateTime getCancelDateTime() {
        return cancelDateTime;
    }

    public void setCancelDateTime(LocalDateTime cancelDateTime) {
        this.cancelDateTime = cancelDateTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
