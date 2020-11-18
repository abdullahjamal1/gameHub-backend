package app.services;

import java.io.File;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import app.Application;
import app.configs.ApplicationConfig;
import app.controllers.LoginController;
import app.models.entity.User;
import app.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private static final int INVALID_ACTIVATION_LENGTH = 5;
   
    @Autowired
    private ApplicationConfig config;

    @Autowired
    private UserRepository repo;

    @Autowired
    private HttpSession httpSession;

    private static final String CURRENT_USER_KEY = "CURRENT_USER";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
   
    /**
     *  overriding method in UserDeatilsService 
     *  
     *  Authenticates if a user with given userName exists in dataBase
     *  
     *  @param username
     *  @return UserDetails
     */

    @Override
    public UserDetails loadUserByUsername(final String username) {

        final User user = repo.findOneByUserNameOrEmail(username, username);

        // if user not found
        if (user == null) {
        	
            throw new UsernameNotFoundException(username);
        }
        if (config.isUserVerification() && !user.getToken().equals("1")) {
        	
            Application.LOGGER.error("User [{}] tried to login but account is not activated yet", username);
            
            throw new UsernameNotFoundException(username + " has not been activated yet");
        }
        
        
        httpSession.setAttribute(CURRENT_USER_KEY, user);
        
        final List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), auth);
    }
    
    /**
     * 	takes username as input and authenticates the user via spring security if the user exists in the dataBase
     * 
     *  @Param username 
     *  @Return
     */

    public void autoLogin(final String username) {

        UserDetails userDetails = this.loadUserByUsername(username);
        
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
       /*
        if (auth.isAuthenticated()) {
        	
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        */
    }
    
    /**
     * 
     * registers a user in the database if any other user with same username or Email is not already present.
     * it also assigns a new activation token for the user and saves it in the database so that it can be sent to the user's Email
     * later for Email verification .
     * 
     * @param user
     * @return
     */

    public User register(final User user) {
        
        user.setPassword(encodeUserPassword(user.getPassword()));

        if (this.repo.findOneByUserName(user.getUserName()) == null && this.repo.findOneByEmail(user.getEmail()) == null) {
        	
            final String activation = createActivationToken(user, false);
            
            user.setToken(activation);
            
            this.repo.save(user);
            
            return user;
        }

        return null;
    }

    /**
     * encodes the password using BCryptPasswordEncoder
     * 
     * @param password takes input clear text password
     * @return returns encoded password
     */
    
    public String encodeUserPassword(final String password) {

        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        return passwordEncoder.encode(password);
    }

    /**
     *  Deletes user from the database with particular id
     * 
     * @param id Long : id of user to be deleted
     * @return returns true if user deletion is successful
     */
    
    public Boolean delete(final Long id) {

        this.repo.deleteById(id);
        
        return true;
    }

    /**
     * returns NULL if user's Email id is already verified ( that is activation token = 1).
     * 
     * Verifies user's Email ( by searching from database if any unVerified user has same authentication token as input, if found 
     * then authenticates the user by setting token in database as 1 ) if not yet verified.
     * 
     * @param activation takes as input activation token
     * @return
     */
    
    public User activate(final String activation) {
    	
    	if ("1".equals(activation) || activation.length() < INVALID_ACTIVATION_LENGTH) {
        	
            return null;
        }
        final User u = this.repo.findOneByToken(activation);
        
        if (u != null) {
        	
            u.setToken("1");
            this.repo.save(u);
            return u;
        }
        return null;
    }

    /**
     * encodes a new activation token for an un-Authenticated user by combination of userName, Email and appSecret
     * if save is TRUE then saves the newly generated token corresponding to the user in the database too
     * Returns the newly created activation token of type String
     * 
     * @param user 
     * @param save Set it to TRUE if token is to be saved in database
     * @return encoded activation token of type String
     */
    
    public String createActivationToken(final User user, final boolean save) {
    	
    	String toEncode = user.getEmail() + user.getUserName() + config.getSecret();
        
        final String activationToken = DigestUtils.md5DigestAsHex(toEncode.getBytes(Charsets.UTF_8));
        
        if (save) {
        	
            user.setToken(activationToken);
            
            this.repo.save(user);
        }
        return activationToken;
    }

    /**
     * finds user by Email and creates activation token for that user
     * returns user object if it exists in database, else returns NULL
     * 
     * @param email
     * @return
     * @see createActivationToken()
     */
    
    public User resetActivation(final String email) {

        final User user = this.repo.findOneByEmail(email);
        
        if (user != null) {
        	
            createActivationToken(user, true);
            return user;
        }
        return null;
    }

    /**
     * resets password for a particular user in database
     * 
     * @param user
     * @return TRUE if user found else FALSE
     */
    
    public Boolean resetPassword(User user) {

        final User u = this.repo.findOneByUserName(user.getUserName());
        
        if (u != null) {
        	
            u.setPassword(encodeUserPassword(user.getPassword()));
            u.setToken("1");
            this.repo.save(u);
            return true;
        }
        return false;
    }
    
    
    
    /**
     * updates user details in database
     * 
     * @param userName
     * @param newData
     */

    public void updateUser(String userName, User user) {

        this.repo.updateUser(
        		
                user.getUserName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getCompanyName()
                
        		);
    }

    /**
     * calls logged in user with forceFresh as false
     * 
     * @return Logged in user Object
     */
    
    public User getLoggedInUser() {

        return getLoggedInUser(false);
    }
    
    /**
     * retrieves the user object of the user who is currently logged in, from the SESSION ID TOKEN of the user's SESSION
     * 
     * @param forceFresh boolean
     * @return
     */

    public User getLoggedInUser(boolean forceFresh) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        
        User user = (User) httpSession.getAttribute(CURRENT_USER_KEY);
        
        if (forceFresh || httpSession.getAttribute(CURRENT_USER_KEY) == null) {
        	
            user = this.repo.findOneByUserName(userName);
            
            httpSession.setAttribute(CURRENT_USER_KEY, user);

        }
        return user;
    }

    
    
    /**
     * updates login TIME_STAMP for userName in database
     * 
     * @param userName
     */

    public void updateLastLogin(String userName) {

        this.repo.updateLastLogin(userName);
    }
    
    /**
     * Updates profile picture name for a particular user in database
     * 
     * @param user
     * @param profilePicture Name of profile picture in String type
     */

    public void updateProfilePicture(User user, String profilePicture) {

        this.repo.updateProfilePicture(user.getUserName(), profilePicture);
    }
    
    public void createSaveDirectory(final String saveDirectory) {

        final File test = new File(saveDirectory);
        
        if (!test.exists()) {
        	
            try {
            	
                test.mkdirs();
                
            } catch (final Exception e) {
            	
                LOGGER.error("Error creating user directory", e);
            }
        }
    }
}