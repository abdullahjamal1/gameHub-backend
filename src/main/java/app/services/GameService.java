package app.services;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.configs.ApplicationConfig;
import app.controllers.LoginController;
import app.models.entity.Game;
import app.models.entity.User;
import app.repositories.GameRepository;
import app.repositories.UserRepository;
import apps.models.projections.GameInfo;
import apps.models.projections.GameInfoAbstract;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ApplicationConfig config;
    
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    public List<GameInfoAbstract> findGameList(){
    	
    	return gameRepo.findAllGame();
    }
    
    public List<GameInfoAbstract> findGameListByGenreId(Long genre_id){
    	
    	return gameRepo.findAllGameByGenre(genre_id);
    }
    
    public GameInfo findGameById(Long game_id) {
    	
    	return gameRepo.findOneByGameId(game_id);
    }
    
    public boolean isAuthorOrAdmin(Long game_id) {
    	
        Long gameAuthorUserId = gameRepo.findGameAuthorUserId(game_id);
        final User loggedInUser = userService.getLoggedInUser();       
        /*
         *  if @AuthorOfGame OR @Admin allow to delete the game else deny
         */
        if (loggedInUser.getId().equals(gameAuthorUserId) || loggedInUser.isAdmin()) {
        	
        	return true;
            
        } else{
        	
            return false;
        }
    }
    
    public boolean isAdmin(){
    	
        final User loggedInUser = userService.getLoggedInUser(); 
        if (loggedInUser.isAdmin()) {
        	
        	return true;
            
        } else{
        	
            return false;
        }
    }

	public List<GameInfoAbstract> findGameListFilteredByRating(float rating) {
		
		return gameRepo.gameListWithRatingAbove(rating);
	}

	public byte[] getGameFileById(Long game_id) throws FileNotFoundException, IOException {
		
        final User u = userService.getLoggedInUser();
        
        final String gameFile = config.getUserRoot() + File.separator + u.getId() + File.separator + game_id + File.separator + "game";
        
        return returnGameFile(game_id, gameFile);
	}

	public String createNewGame(MultipartFile file, MultipartFile pic, Game game) {
		// TODO Auto-generated method stub
		gameRepo.save(game);
		
		// saving pic of game
		
		String picName = "snapShot";
        final User user = userService.getLoggedInUser();
        
        if (!pic.isEmpty()) {
        	
            try {        	
                final String saveDirectory = config.getUserRoot() + File.separator + user.getId() + File.separator + game.getGame_id() + File.separator;
                userService.createSaveDirectory(saveDirectory);

                final byte[] bytes = pic.getBytes();

                final ByteArrayInputStream imageInputStream = new ByteArrayInputStream(bytes);
                final BufferedImage image = ImageIO.read(imageInputStream);
                final BufferedImage thumbnail = Scalr.resize(image, 200);

                final File thumbnailOut = new File(saveDirectory + picName);
                ImageIO.write(thumbnail, "png", thumbnailOut);

                userService.updateProfilePicture(user, picName);
                userService.getLoggedInUser(true);
                
                //log.debug("Image Saved::: {}", picName);
                
            } catch (final Exception e) {
            	
                //log.error("Error Uploading File", e);
            }
        }
        
        if (!file.isEmpty()) {
            //filter for checking file extewnsion
            if(file.getContentType().equalsIgnoreCase("image/jpg") || file.getContentType().equalsIgnoreCase("image/jpeg")){

            	
	            try {
	                byte[] bytes = file.getBytes();
	                String filePath = config.getUserRoot() + File.separator + user.getId() + File.separator + game.getGame_id() + File.separator;
	                userService.createSaveDirectory(filePath);
	                
	                BufferedOutputStream stream =
	                        new BufferedOutputStream(new FileOutputStream(new File(filePath + "game")));
	                
	                stream.write(bytes);
	                stream.close();
	
	                //console call
	            }catch (final Exception e) {
	            	
	                //log.error("Error Uploading File", e);
	            }
            }
        }
		
        return "game posted !";
	}

	public byte[] getGamePicById(Long game_id) throws IOException {
		
        final User u = userService.getLoggedInUser();
        
        final String gamePicture = config.getUserRoot() + File.separator + u.getId() + File.separator + game_id + File.separator + "snapShot";
        
        return returnGameFile(game_id, gamePicture);
	} 
		
	
	public byte[] returnGameFile(Long game_id, String location) throws FileNotFoundException, IOException {
        
        if (new File(location).exists()) {
        	
            return IOUtils.toByteArray(new FileInputStream(location));
            
        } else {
        	
            return null;
        } 
	}

	public List<GameInfoAbstract> findGameListByupdateDate(Instant date) {
		
		return gameRepo.gameFilterByLastUpdate(date);
	}
}
