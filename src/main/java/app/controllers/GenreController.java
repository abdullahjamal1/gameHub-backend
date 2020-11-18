package app.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.models.entity.Genre;
import app.repositories.GenreRepository;
import app.services.GameService;
import apps.models.projections.GameInfoAbstract;

/**
 * 
 * @author abdullah jamal
 *
 *	GET : /game/genre/{genre_id}
 *
 *	GET : /game/genre/list
 *
 *	@Admin
 *	PUT : /game/genre/{genre_id}
 *
 *	@Admin
 *	POST : /game/genre/list
 *
 *	@Admin
 *	DELETE : game/genre/{genre_id}
 */

@RestController
public class GenreController {
	
    @Autowired
    private GenreRepository genreRepo;
    
    @Autowired
    private GameService gameService;
    
    
	@GetMapping("/game/genre/{genre_id}")
    public Genre displayGenreById(@PathVariable("genre_id") Long id) {

		return genreRepo.findOneGenreById(id);
    }
    
	@GetMapping("/game/genre/list")
    public Iterable<Genre> displayGenreList() {

		return genreRepo.findAll();
    }
	
	@PutMapping("/game/genre")
    public void editGenreById(@RequestBody Genre g) {
		
		if(gameService.isAdmin()) {
			
			genreRepo.updateGenre(g.getGenre_id(), g.getType());
		}
    }	
	
	@PostMapping("/game/genre/list")
    public String createNewGenreType(@RequestBody Genre g) {
		
		if(gameService.isAdmin()) {
			
			genreRepo.save(g);
			
			return "new genre created successfully";
		}
		else{
			
			return "You are not authorized !";
		}
    }
	
	@DeleteMapping("/game/genre/{genre_id}")
    public String deleteGenreById(@PathVariable Long id) {

		if(gameService.isAdmin()) {
		
			genreRepo.deleteById(id);
			
			return "new genre deleted successfully";			
		}
		else {
			return "You are not authorized !";			
		}
    }	
}
