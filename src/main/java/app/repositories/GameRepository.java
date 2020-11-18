package app.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.models.entity.Game;
import app.models.entity.User;
import apps.models.projections.GameInfo;
import apps.models.projections.GameInfoAbstract;

@Repository
public interface GameRepository extends CrudRepository<Game, Long>{
	
	public static String gameInfoQuery = 
			"SELECT g.game_id, g.name, g.last_modified, g.game_picture, t.type, rate.rate_table AS rating "
			+ "FROM game g "
			+ "JOIN genre t ON g.genre_id = t.genre_id " + 
			",(SELECT (SUM(r.rating)/COUNT(r.rating)) AS rate_table, r.game_id as gameId from ratings r GROUP BY r.game_id) AS rate "
			+ "WHERE g.game_id = rate.gameId ";
	
	public static String gameInfoQueryAdvanced = 
			"SELECT g.*, t.type, rate.rate_table AS rating, u.userName, u.role "
			+ "FROM game g "
			+ "JOIN genre t ON g.genre_id = t.genre_id "
			+ "JOIN user u ON g.user_id = u.id" + 
			",(SELECT (SUM(r.rating)/COUNT(r.rating)) AS rate_table, r.game_id as gameId from ratings r GROUP BY r.game_id) AS rate "
			+ "WHERE g.game_id = rate.gameId ";
	
	@Query(value = gameInfoQuery, nativeQuery = true)
	public List<GameInfoAbstract> findAllGame();	
	
	public static String gameInfoQueryByGenre = gameInfoQuery + "AND g.genre_id = :genreId;";
	
	@Query(value = gameInfoQueryByGenre, nativeQuery = true)
	public List<GameInfoAbstract> findAllGameByGenre(@Param("genreId") Long genre_id);	
	
	public static String gameInfoQueryById = gameInfoQueryAdvanced + "AND g.game_id = :gameId;";
	
	@Query(value = gameInfoQueryById, nativeQuery = true)
	public GameInfo findOneByGameId(@Param("gameId") Long game_id);	
	
	public static String findQueryUserIdByGameId = "SELECT u.id FROM User u, Game g "
			+ "WHERE g.game_id = :gameId AND g.user_id = u.id;";
	
	@Query(value = findQueryUserIdByGameId, nativeQuery = true)
	public Long findGameAuthorUserId(@Param("gameId") Long game_id);

	public static String findGamesWithRatingsAbove = gameInfoQuery + " AND rating >= :rating;";
	
	@Query(value = findGamesWithRatingsAbove, nativeQuery = true)	
	public List<GameInfoAbstract> gameListWithRatingAbove(@Param("rating") float rating);

	public static String queryToFilterGameByUpdateDate = gameInfoQuery + " AND g.last_modified >= :date;";	
	
	@Query(value = queryToFilterGameByUpdateDate, nativeQuery = true)
	public List<GameInfoAbstract> gameFilterByLastUpdate(@Param("date") Instant date);	
	
}
