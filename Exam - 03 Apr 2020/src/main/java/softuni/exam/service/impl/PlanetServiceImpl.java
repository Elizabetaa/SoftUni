package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.PlaneImportDto;
import softuni.exam.models.dtos.xmls.PlanesImportRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlanetServiceImpl implements PlaneService {
    private final static String PLANE_PATH = "src/main/resources/files/xml/planes.xml";
    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;

    @Autowired
    public PlanetServiceImpl(PlaneRepository planeRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PLANE_PATH)));
    }

    @Override
    public String importPlanes() throws JAXBException {
        StringBuilder builder = new StringBuilder();
        PlanesImportRootDto planeImportDto = this.xmlParser.parseXml(PlanesImportRootDto.class, PLANE_PATH);

        for (PlaneImportDto importDto : planeImportDto.getPlane()) {
            if (validationUtil.isValid(importDto)){
                Plane plane = this.modelMapper.map(importDto,Plane.class);
                this.planeRepository.saveAndFlush(plane);
                builder.append(String.format("Successfully imported Plane %s",importDto.getRegisterNumber()))
                        .append(System.lineSeparator());
            }else {
                builder.append("Invalid Plane")
                        .append(System.lineSeparator());
            }
        }
        return builder.toString();
    }
}
