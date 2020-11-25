package softuni.exam.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity {
    private String serialNumber;
    private BigDecimal price;
    private LocalDateTime takeoff;
    private Town fromTownId;
    private Passenger passenger;
    private Plane plane;
    private Town toTownId;


    public Ticket() {
    }

    @Column(name = "serial_number",unique = true)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column
    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    @ManyToOne
    @JoinColumn(name = "from_town_id")
    public Town getFromTownId() {
        return fromTownId;
    }

    public void setFromTownId(Town fromTownId) {
        this.fromTownId = fromTownId;
    }

    @ManyToOne()
    @JoinColumn(name = "passenger_id")
    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    @ManyToOne
    @JoinColumn(name = "plane_id")
    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
    @ManyToOne
    @JoinColumn(name = "to_town_id")
    public Town getToTownId() {
        return toTownId;
    }

    public void setToTownId(Town toTownId) {
        this.toTownId = toTownId;
    }
}

