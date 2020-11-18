package com.example.demo.services.Impls;

import com.example.demo.domain.dtos.GameAddDto;
import com.example.demo.domain.dtos.GameEditDto;
import com.example.demo.domain.entities.Game;
import com.example.demo.domain.entities.User;
import com.example.demo.repositories.GameRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.GameService;
import com.example.demo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private  final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper, UserService userService, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {

        if (this.userService.isLoggedUserIsAdmin()){
            Game game = this.modelMapper
                    .map(gameAddDto,Game.class);
            this.gameRepository.saveAndFlush(game);
        }else  {
            System.out.println("Logged user is not admin");
        }


    }

    @Override
    public void editGame(GameEditDto gameEditDto) {
        Game game = this.gameRepository.findById(gameEditDto.getId()).orElse(null);
        System.out.println();
        if (!this.userService.isLoggedUserIsAdmin()){
            System.out.println("Logged user is not admin");
        }
        if (game != null){
            game.setPrice(gameEditDto.getPrice());
            game.setSize(gameEditDto.getSize());
            this.gameRepository.saveAndFlush(game);
        }
    }

    @Override
    public Game getGameById(Long id) {
        return this.gameRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        if (!this.userService.isLoggedUserIsAdmin()){
            System.out.println("Logged user is not admin");
        }
        this.gameRepository.deleteById(id);
    }

    @Override
    public List<Game> getAll() {
        return this.gameRepository.getAll();
    }

    @Override
    public Game findByTitle(String title) {
        return this.gameRepository.findByTitle(title);
    }

    @Override
    public String  getAllByLoggedInUser() {
        StringBuilder builder = new StringBuilder();
        User user = this.userRepository.findAllByEmail(this.userService.getLoggedInUserByEmail());
        if (user == null){
           builder.append("Don't have logged in user");

        }else {
            user.getGames().forEach( g ->{
                builder.append(g.getTitle()).append(System.lineSeparator());
            });
        }
        return builder.toString();
    }
}
