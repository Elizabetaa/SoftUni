package softuni.exam.models.dtos.xmls;

import org.hibernate.validator.constraints.Length;
import softuni.exam.config.LocaleDateTimeAdapter;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketImportDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "take-off")
    @XmlJavaTypeAdapter(LocaleDateTimeAdapter.class)
    private LocalDateTime takeoff;
    @XmlElement(name = "from-town")
    private FromTownXmlDto fromTown;
    @XmlElement(name = "to-town")
    private ToTownXmlDto toTown;
    @XmlElement(name = "passenger")
    private PassengerEmailXmlDto passengerEmail;
    @XmlElement(name = "plane")
    private PlaneRegisterNumberXmlDto planeRegisterNumber;

    public TicketImportDto() {
    }

    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Min(value = 0)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    public FromTownXmlDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(FromTownXmlDto fromTown) {
        this.fromTown = fromTown;
    }

    public ToTownXmlDto getToTown() {
        return toTown;
    }

    public void setToTown(ToTownXmlDto toTown) {
        this.toTown = toTown;
    }

    public PassengerEmailXmlDto getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(PassengerEmailXmlDto passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public PlaneRegisterNumberXmlDto getPlaneRegisterNumber() {
        return planeRegisterNumber;
    }

    public void setPlaneRegisterNumber(PlaneRegisterNumberXmlDto planeRegisterNumber) {
        this.planeRegisterNumber = planeRegisterNumber;
    }
}
