package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import app.models.entity.Message;
import app.repositories.MessageRepository;
import app.services.MessageService;
import app.services.UserService;
import apps.models.projections.MessageInfo;

/**
 * 
 * GET : /game/{game_id}/messages
 * 
 */

@RestController
public class MessageController{
	
	@Autowired
	private MessageService messageService;	
	
	@GetMapping("/game/{game_id}/messages")
	public List<MessageInfo> findAllMessages(@PathVariable("game_id") Long game_id) {
		
		return messageService.findAllMessages(game_id);
	}
	
	/**
	 * @Admin
	 * @AuthorOfMessage
	 * 
	 * @param msg_id
	 * @return
	 */
	
	@DeleteMapping("/game/messages/{msg_id}")
	public String deleteMessageById(@PathVariable("msg_id") Long msg_id) {
		
		return messageService.deleteMessageById(msg_id);
	}
	
	/**
	 * vote = 1 for upvote
	 * vote = 0 for downVote
	 * 
	 * @param msg_id
	 * @param vote
	 * @return
	 */
	
	@PutMapping("/game/messages/{msg_id}/vote/")
	public String voteMessageById(@PathVariable("msg_id") Long msg_id,
								  @RequestBody boolean vote) {
		
		return messageService.voteMessage(msg_id, vote);
	}
	
	
	@PostMapping("/game/messages")
	public String postMessage(@RequestBody Message msg) {
		
		return messageService.save(msg);
	}
	
	
}
