package com.example.demo.services;

import com.example.demo.domain.dtos.GameAddDto;
import com.example.demo.domain.dtos.GameEditDto;
import com.example.demo.domain.entities.Game;

import java.util.List;

public interface GameService {

    void addGame(GameAddDto gameAddDto);

    void editGame(GameEditDto gameEditDto);

    Game getGameById(Long id);

    void delete(Long id);

    List<Game> getAll();

    Game findByTitle(String title);

   String getAllByLoggedInUser();
}
