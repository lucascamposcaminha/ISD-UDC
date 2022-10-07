package es.udc.ws.app.model.reserva;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {
    private Long idReserva;
    private Long idExcursion;
    private String userEmail;
    private int numPlaces;
    private String numCreditCard;
    private LocalDateTime registerDateTime;
    private LocalDateTime cancelDateTime;
    private float price;

    // Este es para devolver reservas
    public Reserva(Long idReserva, Long idExcursion, String userEmail, int numPlaces, String numCreditCard,
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

    // Este es para crear reservas
    public Reserva(Long idExcursion, String userEmail, int numPlaces, String numCreditCard,
                   LocalDateTime registerDateTime, float price) {
        this.idExcursion = idExcursion;
        this.userEmail = userEmail;
        this.numPlaces = numPlaces;
        this.numCreditCard = numCreditCard;
        this.registerDateTime = registerDateTime;
        this.price = price;
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
    public boolean equals(Object obj){
        if(this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reserva reserva = (Reserva) obj;

        return Objects.equals(idReserva, reserva.idReserva) && Objects.equals(registerDateTime, reserva.registerDateTime) &&
                numPlaces == reserva.numPlaces && Objects.equals(numCreditCard, reserva.numCreditCard) &&
                Objects.equals(idExcursion, reserva.idExcursion) && Objects.equals(cancelDateTime, reserva.cancelDateTime) &&
                Objects.equals(userEmail, reserva.getUserEmail());
    }

    @Override
    public int hashCode(){
        return Objects.hash(idReserva,registerDateTime,numPlaces,numCreditCard,idExcursion,cancelDateTime);
    }
}