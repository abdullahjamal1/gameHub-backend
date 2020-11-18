package app.repositories;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import app.models.entity.Msg_vote;
import app.models.entity.Msg_voteId;

@Repository
public interface Msg_voteRepository extends CrudRepository<Msg_vote, Msg_voteId>{

	public static String hasAlreadyVoted = "SELECT IF( "
			+ "SELECT COUNT(*) FROM msg_vote WHERE msg_vote.user_id = :id ) > 0, 'TRUE', 'FALSE') "
			+ "FROM msg_vote;";
	
	@Query(value = hasAlreadyVoted, nativeQuery = true)
	boolean hasAlreadyVoted(@Param("id") Long id);

}
