package app.models.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Game{

    @GeneratedValue
    @Id
	private Long game_id;
	private String name; 
	
	private Instant creationTime;

    private Instant lastModified;
    
    private Long user_id;
	private Long genre_id;
	private String game_path;
	
	private String description;
	private String game_picture;
	
	public Game() {
		
	}

    public Game(Long game_id, String name, Instant creationTime, Instant lastModified, Long user_id, Long genre_id,
			String game_path, String description, String game_picture) {
		super();
		this.game_id = game_id;
		this.name = name;
		this.creationTime = creationTime;
		this.lastModified = lastModified;
		this.user_id = user_id;
		this.genre_id = genre_id;
		this.game_path = game_path;
		this.description = description;
		this.game_picture = game_picture;
	}

	@PrePersist
    void createdAt() {

        final Instant i = Instant.now();
        creationTime = i;
        lastModified = i;
    }

    @PreUpdate
    void lastModified() {

        setLastModified(Instant.now());
    }
    
    public Long getGame_id() {
		return game_id;
	}

	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getGenre_id() {
		return genre_id;
	}

	public void setGenre_id(Long genre_id) {
		this.genre_id = genre_id;
	}

	public String getGame_path() {
		return game_path;
	}

	public void setGame_path(String game_path) {
		this.game_path = game_path;
	}

	public Instant getCreationTime() {

        return creationTime;
    }

    public void setCreationTime(final Instant creationTime) {

        this.creationTime = creationTime;
    }
    

    public Instant getLastModified() {

        return lastModified;
    }

    public void setLastModified(final Instant lastModified) {

        this.lastModified = lastModified;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGame_picture() {
		return game_picture;
	}

	public void setGame_picture(String game_picture) {
		this.game_picture = game_picture;
	}
	
}
