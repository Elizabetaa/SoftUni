package softuni.exam.domain.dtos.xmls;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamImportDto {

    @XmlElement
    private String name;
    @XmlElement (name = "picture")
    private PictureImportDto pictureXmlDto;


    public TeamImportDto() {
    }

    @NotNull
    @Length(min = 3,max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureImportDto getPictureXmlDto() {
        return pictureXmlDto;
    }

    public void setPictureXmlDto(PictureImportDto pictureXmlDto) {
        this.pictureXmlDto = pictureXmlDto;
    }
}
