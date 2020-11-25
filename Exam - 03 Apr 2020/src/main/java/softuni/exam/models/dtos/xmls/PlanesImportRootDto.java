package softuni.exam.models.dtos.xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanesImportRootDto {

    @XmlElement(name = "plane")
    private List<PlaneImportDto> plane;

    public PlanesImportRootDto() {
    }

    public List<PlaneImportDto> getPlane() {
        return plane;
    }

    public void setPlane(List<PlaneImportDto> plane) {
        this.plane = plane;
    }
}
