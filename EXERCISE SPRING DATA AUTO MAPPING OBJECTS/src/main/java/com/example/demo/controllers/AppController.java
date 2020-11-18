package com.example.demo.controllers;

import com.example.demo.domain.dtos.*;
import com.example.demo.domain.entities.Game;
import com.example.demo.services.GameService;
import com.example.demo.services.UserService;
import com.example.demo.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppController implements CommandLineRunner {
    private final BufferedReader reader;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final GameService gameService;


    @Autowired
    public AppController(BufferedReader reader, ValidationUtil validationUtil, UserService userService, GameService gameService) {
        this.reader = reader;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Enter command:");
            String[] input = this.reader.readLine().split("\\|");

            switch (input[0]) {
                case "RegisterUser":
                    if (!input[2].equals(input[3])) {
                        System.out.println("Password don't match");
                        break;
                    }
                    UserRegisterDto userRegisterDto =
                            new UserRegisterDto(input[1], input[2], input[4]);

                    if (this.validationUtil.isValid(userRegisterDto)) {
                        this.userService.registerService(userRegisterDto);
                        System.out.printf("%s was registered", input[4]);
                    } else {
                        this.validationUtil
                                .getValidation(userRegisterDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                    break;
                case "LoginUser":
                    UserLoginDto userLoginDto = new UserLoginDto(input[1], input[2]);
                    this.userService.loginUser(userLoginDto);
                    break;
                case "Logout":
                    this.userService.logout();
                    break;
                case "AddGame":
                    GameAddDto gameAddDto = new GameAddDto(
                            input[1], new BigDecimal(input[2]),
                            Double.parseDouble(input[3]),
                            input[4], input[5], input[6],
                            LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    );
                    if (this.validationUtil.isValid(gameAddDto)) {
                        try {
                            this.gameService.addGame(gameAddDto);
                            System.out.printf("Added %s%n", input[1]);
                        } catch (NullPointerException e) {
                            System.out.println("Dont have logged in user");
                        }
                    } else {
                        this.validationUtil
                                .getValidation(gameAddDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                    break;
                case "EditGame":
                    try {
                        GameEditDto gameEditDto =
                                new GameEditDto(Long.parseLong(input[1]),
                                        new BigDecimal(input[2].replace("price=", "")),
                                        Double.parseDouble(input[3].replace("size=", "")));
                        this.gameService.editGame(gameEditDto);
                        if (gameService.getGameById(Long.parseLong(input[1])) != null) {
                            System.out.printf("Edited %s%n", gameService.getGameById(Long.parseLong(input[1])).getTitle());
                        }else {
                            System.out.println("Invalid game");
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Dont have logged in user");
                    }
                    break;
                case "DeleteGame":
                    try {
                        this.gameService.delete(Long.parseLong(input[1]));
                        System.out.printf("Deleted %s%n",gameService.getGameById(Long.parseLong(input[1])).getTitle());
                    } catch (NullPointerException e) {
                        System.out.println("Dont have logged in user");
                    }
                    break;
                case "AllGames":
                    List<GameViewDto> gameViewDtos = new ArrayList<>();
                    this.gameService.getAll().forEach(g -> {
                        GameViewDto gameViewDto = new GameViewDto(g.getTitle(),g.getPrice());
                        gameViewDtos.add(gameViewDto);
                    });

                    gameViewDtos.forEach(g ->{
                        System.out.printf("%s %.2f%n",g.getTitle(),g.getPrice());
                    });
                    break;
                case "DetailGame":
                    Game game = this.gameService.findByTitle(input[1]);
                    if (game == null){
                        System.out.println("Invalid game title");
                        break;
                    }
                    GameInfo gameInfo = new GameInfo(game.getTitle(),game.getPrice(),game.getDescription(),game.getReleaseDate());

                    System.out.printf("Title: %s%n" +
                            "Price: %.2f%n" +
                            "Description: %s%n" +
                            "Release date: %s%n",gameInfo.getTitle(),gameInfo.getPrice(),
                            gameInfo.getDescription(),gameInfo.getReleaseDate());
                    break;
                case "OwnedGames":
                    String output = this.gameService.getAllByLoggedInUser();

                    System.out.println(output);
                    break;
            }

        }

    }

}
