package apps.models.projections;

import java.time.Instant;

public interface LeaderboardInfo {
	
	/*
	 *  from leaderboard
	 */
	
	public Long getUser_id();

	public void setUser_id(Long user_id);

	public Long getGame_id();

	public void setGame_id(Long game_id);

	public Long getScore();

	public void setScore(Long score);
	
    public Instant getLastModified();

    public void setLastModified(final Instant lastModified);
    
    /*
     *  from User
     */
    
    public String getUserName();
    public void setUserName(final String name);
}
