package app.controllers;


import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.configs.ApplicationConfig;
import app.models.entity.User;
import app.repositories.UserRepository;
import app.services.MailService;
import app.services.UserService;

/*
 * 
 * GET  /login
 * GET  /user/register
 * POST /user/register  ???????????
 * GET  /user/reset-password
 * POST /user/reset-password
 * GET  /user/reset-password-change
 * GET  /user/activation-send
 * POST /user/activation-send
 * GET  /user/activate
 * 
 */

@Controller
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    public static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login(final User user) {

        return "user/login";
    }

    @GetMapping("/user/register")
    
    public String register(final User user) {

        return "user/register";
    }

    @PostMapping(value = "/user/register")
    
    public String registerPost(@Valid User user, final BindingResult result) {

        if (result.hasErrors()) {
        	
            return "user/register";
        }

        User registeredUser = userService.register(user);
        
        if (registeredUser != null) {
        	
            mailService.sendNewRegistration(user.getEmail(), registeredUser.getToken());
            
            if (!config.isUserVerification()) {
            	
                userService.autoLogin(user.getUserName());
                return "redirect:/";
            }
            
            return "user/register-success";
            
        } else {
        	
            log.error("User already exists: {}", user.getUserName());
            
            result.rejectValue("email", "error.alreadyExists", "This username or email already exists, please try to reset password instead.");
            
            return "user/register";
        }
    }

    @GetMapping("/user/reset-password")
    
    public String resetPasswordEmail(User user) {

        return "user/reset-password";
    }

    @PostMapping("/user/reset-password")
    
    public String resetPasswordEmailPost(User user, final BindingResult result) {

        final User u = userRepository.findOneByEmail(user.getEmail());
        
        if (u == null) {
        	
            result.rejectValue("email", "error.doesntExist", "We could not find this email in our databse");
            
            return "user/reset-password";
            
        } else {
        	
            final String resetToken = userService.createActivationToken(u, true);
            
            LOGGER.debug("Resetting password for user: {}, new token: {}", user.getEmail(), resetToken);
            
            mailService.sendResetPassword(user.getEmail(), user.getFirstName(), resetToken);
        }
        return "user/reset-password-sent";
    }

    @GetMapping("/user/reset-password-change")
    
    public String resetPasswordChange(final User user, final BindingResult result, final Model model) {

        final User u = userRepository.findOneByToken(user.getToken());
        
        if (user.getToken().equals("1") || u == null) {
        	
            result.rejectValue("activation", "error.doesntExist", "We could not find this reset password request.");
            
        } else {
        	
            model.addAttribute("userName", u.getUserName());
        }
        return "user/reset-password-change";
    }

    @PostMapping("/user/reset-password-change")
    
    public String resetPasswordChangePost(final User user, final BindingResult result, final Model model) {

        final boolean isChanged = userService.resetPassword(user);
        
        if (isChanged) {
        	
            userService.autoLogin(user.getUserName());
            return "redirect:/";
            
        } else {
        	
            model.addAttribute("error", "Password could not be changed");
            
            return "user/reset-password-change";
        }
    }

    @GetMapping("/user/activation-send")
    
    public String activationSend(final User user) {

        return "/user/activation-send";
    }

    @PostMapping("/user/activation-send")
    
    public String activationSendPost(final User user, final BindingResult result) {

        final User u = userService.resetActivation(user.getEmail());
        
        if (u != null){
        	
            mailService.sendNewActivationRequest(u.getEmail(), u.getToken());
            
            return "/user/activation-sent";
            
        } else {
        	
            result.rejectValue("email", "error.doesntExist", "We could not find this email in our databse");
            
            return "/user/activation-send";
        }
    }

    @GetMapping("/user/activate")
    
    public String activate(final String activation) {

        final User u = userService.activate(activation);
        
        if (u != null) {
        	
            userService.autoLogin(u.getUserName());
            return "redirect:/";
        }
        return "redirect:/error?message=Could not activate with this activation code, please contact support";
    }

    
}
