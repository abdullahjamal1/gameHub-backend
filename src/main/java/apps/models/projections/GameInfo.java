package apps.models.projections;

import java.time.Instant;

public interface GameInfo extends GameInfoAbstract{
	
	/*
	 * 
	 *  from game entity
	 */

	public Long getUser_id();
	public void setUser_id(Long user_id);

	public Long getGenre_id();
	public void setGenre_id(Long genre_id);

	public String getGame_path();
	public void setGame_path(String game_path);

	public Instant getCreationTime();
    public void setCreationTime(final Instant creationTime);

	public String getDescription();
	public void setDescription(String description);

	public String getGame_picture();
	public void setGame_picture(String game_picture);
	
	/*
	 *  from genre User
	 */
    public String getUserName();
    public void setUserName(final String name);
    
    public String getRole();
    public void setRole(final String role);
	
}
