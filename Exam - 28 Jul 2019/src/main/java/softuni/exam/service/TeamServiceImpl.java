package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.xmls.PictureImportDto;
import softuni.exam.domain.dtos.xmls.TeamImportDto;
import softuni.exam.domain.dtos.xmls.TeamImportRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TeamServiceImpl implements TeamService {
    private final static String TEAM_PATH = "src/main/resources/files/xml/teams.xml";
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final PictureRepository pictureRepository;
    private final ValidatorUtil validatorUtil;
    private final TeamRepository teamRepository;

    @Autowired
    public TeamServiceImpl(XmlParser xmlParser, ModelMapper modelMapper, PictureRepository pictureRepository, ValidatorUtil validatorUtil, TeamRepository teamRepository) {
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.pictureRepository = pictureRepository;
        this.validatorUtil = validatorUtil;
        this.teamRepository = teamRepository;
    }

    @Override
    public String importTeams() throws JAXBException {
        StringBuilder builder = new StringBuilder();

        TeamImportRootDto teamImportRootDto = this.xmlParser.parseXml(TeamImportRootDto.class, TEAM_PATH);
        for (TeamImportDto teamImportDto : teamImportRootDto.getTeamImportDtos()) {
            Picture picture = this.pictureRepository.findByUrl(teamImportDto.getPictureXmlDto().getUrl());

            if (validatorUtil.isValid(teamImportDto) && picture != null){
                Team team = this.modelMapper.map(teamImportDto,Team.class);
                team.setPicture(picture);
                this.teamRepository.saveAndFlush(team);

                builder.append(String.format("Successfully imported - %s%n",teamImportDto.getName()));
            }else {
                builder.append("Invalid team").append(System.lineSeparator());
            }
        }

        return builder.toString();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(TEAM_PATH)));
    }
}
