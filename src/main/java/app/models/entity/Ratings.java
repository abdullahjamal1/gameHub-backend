package app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.mapping.Selectable;

@Entity
public class Ratings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1746979225792481600L;
	
	@GeneratedValue
	@Id
	private Long user_id;
	private float rating;
	@Id
	private Long game_id;
	
	public Ratings() {
		
	}
	
	public Ratings(Long user_id, int rating, Long game_id) {
		super();
		this.user_id = user_id;
		this.rating = rating;
		this.game_id = game_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public Long getGame_id() {
		return game_id;
	}
	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}
	
	
}
