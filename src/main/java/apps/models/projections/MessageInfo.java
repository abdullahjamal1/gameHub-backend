package apps.models.projections;

import java.time.Instant;

import javax.persistence.PrePersist;

public interface MessageInfo {
	
	/*
	 *  from Message Entity
	 */
	
	public int getUp_votes();
	public void setUp_votes(int up_votes);
	
	public int getDown_votes();
	public void setDown_votes(int down_votes);

	public Long getMsg_id();
	public void setMsg_id(Long msg_id);
	
	public Long getUser_id();
	public void setUser_id(Long user_id);
	
	public String getDescription();
	public void setDescription(String description);

    public Instant getCreation_time();
    public void setCreation_time(Instant creation_time);
    
    /*
     *  From User Entity
     */
    
    public String getUserName();
    public void setUserName(String name);
    
    /*
     *  custom
     */
    public boolean getHasVoted();
    public void setHasVoted(boolean hasVoted);
}
