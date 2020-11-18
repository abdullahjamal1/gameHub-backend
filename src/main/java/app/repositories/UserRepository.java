package app.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import app.models.entity.User;

/**
 * 
 * @author abdullah jamal
 * 
 * crudRepository< TABLE_NAME_IN_DB, PRIMARY_KEY_DATA_TYPE >
 *
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByUserName(String name);

    User findOneByEmail(String email);

    User findOneByUserNameOrEmail(String username, String email);

    User findOneByToken(String token);
    
    User findOneById(Long id);

    /*
     * Queries that require a `@Modifying` annotation include INSERT, UPDATE, DELETE, and DDLstatements.
     *  @Transactional Describes a transaction attribute on an individual method or on a class
     */
    
    @Modifying
    @Transactional
    @Query("update User u set u.email = :email, u.firstName = :firstName, "
            + "u.lastName = :lastName, u.address = :address, u.companyName = :companyName "
            + "where u.userName = :userName")
    int updateUser(
            @Param("userName") String userName,
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("address") String address,
            @Param("companyName") String companyName
            );

    @Modifying
    @Transactional
    @Query("update User u set u.lastLogin = CURRENT_TIMESTAMP where u.userName = ?1")
    int updateLastLogin(String userName);

    @Modifying
    @Transactional
    @Query("update User u set u.profilePicture = ?2 where u.userName = ?1")
    int updateProfilePicture(String userName, String profilePicture);
}