package app.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import app.configs.ApplicationConfig;
import app.models.entity.User;
import app.repositories.UserRepository;
import app.services.UserService;
//import nz.net.ultraq.thymeleaf.LayoutDialect;

/*
 *  GET  /
 *  GET  /user/edit/{id}    ----> now PUT
 *  GET  /user/list
 *  GET  /user/delete/{id}  -----> now DELETE
 *  GET  /user/autologin
 *  POST  /user/edit  
 * 	POST  /user/upload
 *  GET  /user/profile-picture
 * 
 */

@Controller
public class UserController {
	
    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private UserRepository userRepository;
  

    @Autowired
    private UserService userService;

    public static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    @GetMapping("/")
    public String home(final Map<String, Object> model) {

        LOGGER.debug("Index page hit");

        model.put("message", "Hello World");
        model.put("title", "Hello Home");
        model.put("date", new Date());
        return "index";
    }
    
	@GetMapping("/user/profile/{id}")
	@ResponseBody
    public User display(@PathVariable("id") final Long id) {

		final User user = userRepository.findOneById(id);
		
		if(user == null) {
			
			return null;
		}
        return user;
    }
	
    
	@PutMapping("/user/edit/{id}")
	
    public String edit(@PathVariable("id") final Long id, final User user) {

        final User u;
        Long lookupId = id;
        final User loggedInUser = userService.getLoggedInUser();
        
        if (id == 0) {
        	
            lookupId = loggedInUser.getId();
            
        }
        if (!loggedInUser.getId().equals(lookupId) && !loggedInUser.isAdmin()) {
        	
            return "user/premission-denied";
            
        } else if (loggedInUser.isAdmin()) {
        	
            u = userRepository.findById(lookupId).orElse(null);
            
        } else {
        	
            u = loggedInUser;
        }
        user.setId(u.getId());
        user.setUserName(u.getUserName());
        user.setAddress(u.getAddress());
        user.setCompanyName(u.getCompanyName());
        user.setEmail(u.getEmail());
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());

        return "/user/edit";
    }

    @DeleteMapping("/user/delete/{id}")
    
    public String delete(@PathVariable("id") Long id) {

        userService.delete(id);
        
        return "redirect:/user/list";
    }
    
    @GetMapping("/user/autologin")
    
    public String autoLogin(final User user) {

        userService.autoLogin(user.getUserName());
        
        return "redirect:/";
    }

    @GetMapping("/user/list")
    
    public String list(final ModelMap map) {

        final Iterable<User> users = this.userRepository.findAll();
        
        map.addAttribute("users", users);
        
        return "user/list";
    }

    @PostMapping("/user/edit")
    
    public String edit(@Valid User user, final BindingResult result) {

        if (result.hasFieldErrors("email")) {
        	
            return "/user/edit";
        }
        
        if (userService.getLoggedInUser().isAdmin()) {
        	
            userService.updateUser(user.getUserName(), user);
            
        } else {
        	
            userService.updateUser(userService.getLoggedInUser().getUserName(), user);
        }

        if (userService.getLoggedInUser().getId().equals(user.getId())) {
        	
            // put updated user to session
            userService.getLoggedInUser(true);
        }

        return "redirect:/user/edit/" + user.getId() + "?updated";
    }

    @PostMapping("/user/upload")
    
    public String handleFileUpload(@RequestParam("file") final MultipartFile file) {

        final Format formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        final String fileName = formatter.format(Calendar.getInstance().getTime()) + "_thumbnail.jpg";
        final User user = userService.getLoggedInUser();
        
        if (!file.isEmpty()) {
        	
            try {        	
                final String saveDirectory = config.getUserRoot() + File.separator + user.getId() + File.separator;
                userService.createSaveDirectory(saveDirectory);

                final byte[] bytes = file.getBytes();

                final ByteArrayInputStream imageInputStream = new ByteArrayInputStream(bytes);
                final BufferedImage image = ImageIO.read(imageInputStream);
                final BufferedImage thumbnail = Scalr.resize(image, 200);

                final File thumbnailOut = new File(saveDirectory + fileName);
                ImageIO.write(thumbnail, "png", thumbnailOut);

                userService.updateProfilePicture(user, fileName);
                userService.getLoggedInUser(true);
                
                log.debug("Image Saved::: {}", fileName);
                
            } catch (final Exception e) {
            	
                log.error("Error Uploading File", e);
            }
        }
        return "redirect:/user/edit/" + user.getId();
    }

    @GetMapping(value = "/user/profile-picture", produces = MediaType.IMAGE_JPEG_VALUE)
    
    public @ResponseBody byte[] profilePicture() throws IOException {

        final User u = userService.getLoggedInUser();
        
        final String profilePicture = config.getUserRoot() + File.separator + u.getId() + File.separator + u.getProfilePicture();
        
        if (new File(profilePicture).exists()) {
        	
            return IOUtils.toByteArray(new FileInputStream(profilePicture));
            
        } else {
        	
            return null;
        }
        
    }
}
