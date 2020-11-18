package app.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import app.models.entity.Leaderboard;
import apps.models.projections.LeaderboardInfo;

@RestController
public class LeaderboardController {
	
	//@Autowired
	//LeaderboardService leaderboardService;
	
	@PostMapping("/game/{game_id}/leaderboard")
	public String postScoreToLeaderBoard(@PathVariable("game_Id") Long game_id, @RequestBody Leaderboard l) {
		
		return "this controller is not yet implemented";
	}
	
	@GetMapping("/game/{game_id}/leaderboard")
	public List<LeaderboardInfo> getLeaderboardByGameId(){
		
		return null;
		
	}

}
