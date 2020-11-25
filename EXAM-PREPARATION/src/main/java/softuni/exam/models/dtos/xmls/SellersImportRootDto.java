package softuni.exam.models.dtos.xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellersImportRootDto {

    @XmlElement(name = "seller")
    private List<SellerDto> sellerDto;

    public List<SellerDto> getSellerDto() {
        return sellerDto;
    }

    public void setSellerDto(List<SellerDto> sellerDto) {
        this.sellerDto = sellerDto;
    }

    public SellersImportRootDto() {

    }
}
