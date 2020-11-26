package softuni.exam.domain.dtos.xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class PictureRootImportDto {

    @XmlElement(name = "picture")
    private List<PictureImportDto> pictureImportDto;

    public PictureRootImportDto() {
    }

    public List<PictureImportDto> getPictureImportDto() {
        return pictureImportDto;
    }

    public void setPictureImportDto(List<PictureImportDto> pictureImportDto) {
        this.pictureImportDto = pictureImportDto;
    }
}
