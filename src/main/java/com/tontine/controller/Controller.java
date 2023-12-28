package com.tontine.controller;



import com.tontine.entities.Tontine;
import com.tontine.entities.User;
import com.tontine.repository.UserRepository;
import com.tontine.service.TontineService;
import com.tontine.service.TontineServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
//@org.springframework.stereotype.Controller
//@ResponseBody
@RequestMapping
public class Controller {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TontineService tontineService;

    @GetMapping("/")
    public ModelAndView index(Model model, Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        List<Tontine> tontines = tontineService.findAll();
        int user_id = 0;
        User authenticated_user = null;

        if(authentication != null){
            authenticated_user = userRepository.findByEmail(getLoggedInUserDetails().getUsername()).orElse(null);
        }

        if(authenticated_user != null){
            user_id = authenticated_user.getId();
        }

        model.addAttribute("tontines", tontines);
        model.addAttribute("user_id", user_id);
        modelAndView.setViewName("home");
        return modelAndView;
//        return tontines;
    }

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/user/save")
    public ModelAndView saveUSer(@ModelAttribute("User") User ourUser){
        ourUser.setPassword(passwordEncoder.encode(ourUser.getPassword()));
        User result = userRepository.save(ourUser);

        ModelAndView modelAndView = new ModelAndView("redirect:/login");

        return modelAndView;
    }
    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUSers(){
        return ResponseEntity.ok(userRepository.findAll());
    }
    @GetMapping("/users/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getMyDetails(){
        return ResponseEntity.ok(userRepository.findByEmail(getLoggedInUserDetails().getUsername()));
    }

    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }
}
