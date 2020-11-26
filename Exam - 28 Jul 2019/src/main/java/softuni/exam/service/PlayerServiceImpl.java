package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.jsons.PlayerImportDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final static String TEAM_PATH = "src/main/resources/files/json/players.json";
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final PictureRepository pictureRepository;
    private final ValidatorUtil validatorUtil;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerServiceImpl(Gson gson, ModelMapper modelMapper, PictureRepository pictureRepository, ValidatorUtil validatorUtil, PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.pictureRepository = pictureRepository;
        this.validatorUtil = validatorUtil;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public String importPlayers() throws IOException {
        StringBuilder builder = new StringBuilder();
        PlayerImportDto[] playerImportDto = this.gson.fromJson(readPlayersJsonFile(), PlayerImportDto[].class);

        for (PlayerImportDto importDto : playerImportDto) {
            Player player = this.modelMapper.map(importDto, Player.class);
            Picture picture = this.pictureRepository.findByUrl(importDto.getPicture().getUrl());
            Team team = this.teamRepository.findByName(importDto.getTeam().getName());
            Picture pictureForTeam = this.pictureRepository.findByUrl(importDto.getTeam().getPicture().getUrl());
            if (validatorUtil.isValid(importDto) && picture != null && team != null && pictureForTeam != null) {
                player.setPicture(picture);
                team.setPicture(pictureForTeam);
                player.setTeam(team);
                this.playerRepository.saveAndFlush(player);
                builder.append(String.format("Successfully imported player: %s %s%n",
                        importDto.getFirstName(), importDto.getLastName()));
            } else {
                builder.append("Invalid player").append(System.lineSeparator());
            }

        }


        return builder.toString();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(TEAM_PATH)));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder builder = new StringBuilder();
        Set<Player> players = this.playerRepository.findBySalaryIsAfterOrderBySalaryDesc(new BigDecimal("100000"));

        for (Player player : players) {
            builder.append(String.format("Player name: %s %s%n" +
                            "\tNumber: %d%n" +
                            "\tSalary: %s%n" +
                            "\tTeam: %s%n", player.getFirstNAme(), player.getLastNAme(),
                    player.getNumber(), player.getSalary()
                    , player.getTeam().getName()));
        }

        return builder.toString();
    }

    @Override
    public String exportPlayersInATeam() {

        StringBuilder builder = new StringBuilder();
        Set<Player> players = this.playerRepository.findByTeamNameOrderById("North Hub");
        builder.append("Team: North Hub").append(System.lineSeparator());
        for (Player player : players) {
            builder.append(String.format("\tPlayer name: %s %s - %s%n" +
                    "\tNumber: %d%n", player.getFirstNAme(), player.getLastNAme(), player.getPosition(), player.getNumber()));
        }

        return builder.toString();
    }
}
