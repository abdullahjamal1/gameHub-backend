package app.models.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
public class Message {
	
	@GeneratedValue
	@Id
	private Long msg_id;
	private Long user_id;
	private String description;
    private Instant creation_time;
    private Long game_id;
    
    public Message() {
    	
    }
 
    public Message(Long msg_id, Long user_id, String description, Instant creationTime, Long game_id) {
		super();
		this.msg_id = msg_id;
		this.user_id = user_id;
		this.description = description;
		this.creation_time = creationTime;
		this.game_id = game_id;

	}


	public Long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(Long msg_id) {
		this.msg_id = msg_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getGame_id() {
		return game_id;
	}
	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}
	@PrePersist
    void createdAt() {

        final Instant i = Instant.now();
        creation_time = i;
    }
    public Instant getCreationTime() {

        return creation_time;
    }

    public void setCreationTime(final Instant creationTime) {

        this.creation_time = creationTime;
    }

}
