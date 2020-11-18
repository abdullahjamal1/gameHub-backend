package app.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import app.models.entity.Message;
import apps.models.projections.MessageInfo;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{


	public static String queryToFindMessageAuthor = "SELECT m.user_id "
			+ "FROM message m WHERE m.msg_id = :msg_id; ";
	
	@Query(value = queryToFindMessageAuthor, nativeQuery = true)
	Long findAuthorByMessageId(Long msg_id);
	
	public static String queryfindAllMessagesByGameId = "SELECT m.msg_id, m.user_id, m.description, m.creation_time " + 
			",u.user_name, uc.upvotes as up_votes, (dc.downvotes - uc.upvotes) as down_votes " + 
			",IF((SELECT COUNT(*) FROM msg_vote mv WHERE mv.user_id = :userId AND mv.msg_id = m.msg_id) > 0,'TRUE','FALSE') AS hasVoted " + 
			"FROM message m " + 
			"JOIN user u ON u.id = m.user_id " + 
			",(select sum(v.did_upvote) as upvotes, v.msg_id as id from msg_vote v group by v.msg_id) AS uc " + 
			",(select COUNT(*) as downvotes, v.msg_id as id from msg_vote v group by v.msg_id) AS dc " + 
			"WHERE m.game_id = :gameId " +
			"AND m.msg_id = uc.id " +
			"AND m.msg_id = dc.id " + 
			"GROUP BY m.msg_id; ";
			

	@Query(value = queryfindAllMessagesByGameId, nativeQuery = true)
	List<MessageInfo> findAllMessagesByGameId(@Param("gameId")Long game_id , @Param("userId") Long user_id );

	
}
