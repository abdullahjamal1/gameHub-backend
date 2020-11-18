package apps.models.projections;

import java.time.Instant;

public interface GameInfoAbstract {
	
    public Long getGame_id();
	public void setGame_id(Long game_id);
	
	public String getName();
	public void setName(String name);
	
    public Instant getLastModified();
    public void setLastModified(final Instant lastModified);
    
	public String getGame_picture();
	public void setGame_picture(String game_picture);
	
	public String getType();
	public void setType(String type);
	
	public float getRating();
	public void setRating(float rating);

}
