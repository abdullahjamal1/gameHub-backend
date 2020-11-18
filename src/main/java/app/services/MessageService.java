package app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.models.entity.Message;
import app.models.entity.Msg_vote;
import app.models.entity.User;
import app.repositories.MessageRepository;
import app.repositories.Msg_voteRepository;
import apps.models.projections.MessageInfo;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepository messageRepo;	
    
    @Autowired
    private Msg_voteRepository voteRepo;
     
    @Autowired
    private UserService userService;

	public String deleteMessageById(Long msg_id) {
		
		if(isMessageAuthorOrAdmin(msg_id)) {
			
			messageRepo.deleteById(msg_id);
			
			return "message deleted !";
		}
		else {
			
			return "user not authorized";
		}
	}

	public List<MessageInfo> findAllMessages(Long game_id) {
		
		return messageRepo.findAllMessagesByGameId(game_id, userService.getLoggedInUser().getId());
	}

	public String voteMessage(Long msg_id, boolean vote) {
		
		Msg_vote msgVote = new Msg_vote();
		msgVote.setDid_upvote(vote);
		msgVote.setMsg_id(msg_id);
		msgVote.setUser_id( userService.getLoggedInUser().getId() );
		
		if(!voteRepo.hasAlreadyVoted( userService.getLoggedInUser().getId() )) {
			
			voteRepo.save(msgVote);
		}
		else {
			
			voteRepo.save(msgVote);
		}

		return "your vote has been recorded";
		
	}
	
    public boolean isMessageAuthorOrAdmin(Long msg_id) {
    	
        Long gameAuthorUserId = messageRepo.findAuthorByMessageId(msg_id);
        final User loggedInUser = userService.getLoggedInUser();       
        /*
         *  if @AuthorOfGame OR @Admin allow to delete the game else deny
         */
        if (loggedInUser.getId().equals(gameAuthorUserId) || loggedInUser.isAdmin()) {
        	
        	return true;
            
        } else{
        	
            return false;
        }
    }

	public String save(Message msg) {
		
		msg.setMsg_id(null);

		msg.setUser_id(userService.getLoggedInUser().getId());
		
		messageRepo.save(msg);
		
		return "message saved !";
	}


}
