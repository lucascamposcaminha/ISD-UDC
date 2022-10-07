package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientReservaDto {

    private Long idReserva;
    private Long idExcursion;
    private String userEmail;
    private int numPlaces;
    private String numCreditCard;
    private LocalDateTime registerDateTime;
    private LocalDateTime cancelDateTime;
    private float price;

    public ClientReservaDto() {}

    public ClientReservaDto(Long idReserva, Long idExcursion, String userEmail, int numPlaces, String numCreditCard,
                            LocalDateTime registerDateTime, LocalDateTime cancelDateTime, float price) {
        this.idReserva = idReserva;
        this.idExcursion = idExcursion;
        this.userEmail = userEmail;
        this.numPlaces = numPlaces;
        this.numCreditCard = numCreditCard;
        this.registerDateTime = registerDateTime;
        this.cancelDateTime = cancelDateTime;
        this.price = price;
    }

    public ClientReservaDto(Long idExcursion, String userEmail, int numPlaces, String numCreditCard) {
        this.idExcursion = idExcursion;
        this.userEmail = userEmail;
        this.numPlaces = numPlaces;
        this.numCreditCard = numCreditCard;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(Long idExcursion) {
        this.idExcursion = idExcursion;
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

    public LocalDateTime getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(LocalDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
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

    @Override
    public String toString() {
        return "Reserva {" +
                "idReserva=" + idReserva +
                ", idExcursion=" + idExcursion +
                ", userEmail='" + userEmail +
                ", numPlaces=" + numPlaces +
                ", numCreditCard='" + numCreditCard +
                ", registerDateTime=" + registerDateTime +
                ", cancelDateTime=" + cancelDateTime +
                ", price=" + price + "}";
    }

}
