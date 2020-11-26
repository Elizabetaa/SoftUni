package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.xmls.PictureImportDto;
import softuni.exam.domain.dtos.xmls.PictureRootImportDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;


import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PictureServiceImpl implements PictureService {
    private final static String PICTURE_PATH = "src/main/resources/files/xml/pictures.xml";
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final PictureRepository pictureRepository;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public PictureServiceImpl(XmlParser xmlParser, ModelMapper modelMapper, PictureRepository pictureRepository, ValidatorUtil validatorUtil) {
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.pictureRepository = pictureRepository;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public String importPictures() throws JAXBException {
        StringBuilder builder = new StringBuilder();
        PictureRootImportDto pictureRootImportDto = this.xmlParser.parseXml(PictureRootImportDto.class, PICTURE_PATH);
        for (PictureImportDto pictureImportDto : pictureRootImportDto.getPictureImportDto()) {
            if (validatorUtil.isValid(pictureImportDto)) {
                Picture picture = this.modelMapper.map(pictureImportDto, Picture.class);
                this.pictureRepository.saveAndFlush(picture);
                builder.append(String.format("Successfully imported picture - %s%n", picture.getUrl()));
            } else {
                builder.append("Invalid Picture")
                        .append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PICTURE_PATH)));
    }

}
