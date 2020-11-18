package app.models.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Genre {
	
	@GeneratedValue
	@Id
	private Long genre_id;
	
	private String type;
	
	public Genre() {
		
	}

	public Genre(Long genre_id, String type) {
		super();
		this.genre_id = genre_id;
		this.type = type;
	}

	public Long getGenre_id() {
		return genre_id;
	}

	public void setGenre_id(Long genre_id) {
		this.genre_id = genre_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
