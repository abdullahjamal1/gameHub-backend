package app.controllers;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.repositories.GameRepository;
import app.services.GameService;
import apps.models.projections.GameInfoAbstract;

@RestController
@RequestMapping("/game/filter")
public class GameFilterController {

    @Autowired
    private GameService gameService;
    
	/**
	 * return list of games filtered by genre id
	 * @param genre_id
	 * @return
	 */
	
	@GetMapping("/genre/{genre_id}")
    public List<GameInfoAbstract> displayGameListByGenreId(@PathVariable Long genre_id) {

		return gameService.findGameListByGenreId(genre_id);
    }
	
	@GetMapping("/rating/{rating}")
    public List<GameInfoAbstract> displayGameListFilteredByRating(@PathVariable float rating) {

		return gameService.findGameListFilteredByRating(rating);
    }
	/*
	
	@GetMapping("/creationTime/{date}")
    public List<GameInfoAbstract> displayGameListByGenreId(@PathVariable Long genre_id) {

		return gameService.findGameListByGenreId(genre_id);
    }
    
    */
	
	@GetMapping("/updateTime/{date}")
    public List<GameInfoAbstract> displayGameListFilteredByLastUpdated(@PathVariable Instant date) {

		return gameService.findGameListByupdateDate(date);
    }
    
	
}
