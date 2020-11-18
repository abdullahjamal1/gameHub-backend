package app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(Msg_voteId.class)
public class Msg_vote implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8074411523865875977L;
	
	@Id
	private Long user_id;
	private boolean did_upvote;
	@Id
	private Long msg_id;
	
	public Msg_vote(Long user_id, boolean did_upvote, Long msg_id) {
		super();
		this.user_id = user_id;
		this.did_upvote = did_upvote;
		this.msg_id = msg_id;
	}
	public Msg_vote() {
		// TODO Auto-generated constructor stub
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public boolean isDid_upvote() {
		return did_upvote;
	}
	public void setDid_upvote(boolean did_upvote) {
		this.did_upvote = did_upvote;
	}
	public Long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(Long msg_id) {
		this.msg_id = msg_id;
	}
	
	
}
