package app.models.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Leaderboard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5936949972223384529L;

	@Id
	private Long user_id;
	
	@Id
	private Long game_id;
	
    private Instant lastModified;
    
    private Long score;	
    
    public Leaderboard() {
    	
    }
    
    public Leaderboard(Long user_id, Long game_id, Instant lastModified, Long score) {
		super();
		this.user_id = user_id;
		this.game_id = game_id;
		this.lastModified = lastModified;
		this.score = score;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getGame_id() {
		return game_id;
	}

	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	@PrePersist
    void createdAt() {

        final Instant i = Instant.now();
        lastModified = i;
    }
	
    @PreUpdate
    void lastModified() {

        setLastModified(Instant.now());
    }
	
    public Instant getLastModified() {

        return lastModified;
    }

    public void setLastModified(final Instant lastModified) {

        this.lastModified = lastModified;
    }
	
}
