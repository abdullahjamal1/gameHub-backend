package app.models.entity;

import java.io.Serializable;
import java.util.Objects;

public class Msg_voteId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2967668917688838212L;
	
	private String user_id;
	
	private String msg_id;
	
	public Msg_voteId() {
		
	}

	@Override
	public boolean equals(Object o) {
		
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Msg_voteId msg_voteId = (Msg_voteId) o;
        return user_id.equals(msg_voteId.user_id) &&
        		msg_id.equals(msg_voteId.msg_id);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(user_id, msg_id);
	}

	public Msg_voteId(String user_id, String msg_id) {
		super();
		this.user_id = user_id;
		this.msg_id = msg_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	

}
