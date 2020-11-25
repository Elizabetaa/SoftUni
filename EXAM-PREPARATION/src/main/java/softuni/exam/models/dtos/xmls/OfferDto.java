package softuni.exam.models.dtos.xmls;

import jdk.jfr.BooleanFlag;
import org.hibernate.validator.constraints.Length;
import softuni.exam.config.LocaleDateTimeAdapter;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Seller;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferDto {


    @XmlElement
    private BigDecimal price;
    @XmlElement
    private String description;
    @XmlElement(name = "has-gold-status")
    private boolean hasGoldStatus;
    @XmlElement(name = "added-on")
    @XmlJavaTypeAdapter(LocaleDateTimeAdapter.class)
    private LocalDateTime addedOn;
    @XmlElement(name = "car")
    private CarXMLImportDto car;
    @XmlElement(name = "seller")
    private SellerXMLImportDto seller;

    public OfferDto() {
    }
    @Min(value = 0)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Length(min = 5)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasGoldStatus() {
        return hasGoldStatus;
    }

    public void setHasGoldStatus(boolean hasGoldStatus) {
        this.hasGoldStatus = hasGoldStatus;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public CarXMLImportDto getCar() {
        return car;
    }

    public void setCar(CarXMLImportDto car) {
        this.car = car;
    }

    public SellerXMLImportDto getSeller() {
        return seller;
    }

    public void setSeller(SellerXMLImportDto seller) {
        this.seller = seller;
    }
}
