package com.dobiasdev.spa.controllers.view;
/*
import com.dobiasdev.spa.converters.ConverterManager;
import com.dobiasdev.spa.converters.ConvertingContext;
import com.dobiasdev.spa.converters.NavigationFields;
import com.dobiasdev.spa.model.dtos.UserDto;
import com.dobiasdev.spa.repositories.UserRepository;
import com.dobiasdev.spa.security.payload.AuthenticationRequest;
import com.dobiasdev.spa.security.AuthenticationService;
import com.dobiasdev.spa.security.payload.RegisterRequest;
import com.dobiasdev.spa.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private final UserService userService;
    private final ConverterManager converterManager;
    private final UserRepository userRepository;
    private final AuthenticationService authService;


    public LoginController(UserService userService, ConverterManager converterManager, UserRepository userRepository, AuthenticationService service) {
        this.userService = userService;
        this.converterManager = converterManager;
        this.userRepository = userRepository;
        this.authService = service;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        AuthenticationRequest authRequest = new AuthenticationRequest();
        model.addAttribute("authRequest", authRequest);

        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){

        RegisterRequest user = new RegisterRequest();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") RegisterRequest user,
                               BindingResult result,
                               Model model){

        var userExists = userRepository.existsByEmail(user.getEmail());

        if(userExists){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/register";
        }

        authService.register(user);

        return "redirect:/register?success";
    }

    @PostMapping("/login/save")
    public String login(@Valid @ModelAttribute("user") RegisterRequest user,
                               BindingResult result,
                               Model model){

        var userExists = userRepository.existsByEmail(user.getEmail());

        if(userExists){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/register";
        }

        authService.register(user);

        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.getUsers().stream().map(u -> converterManager.user().convertToDto(u, new NavigationFields(), new ConvertingContext())).collect(Collectors.toList());;
        model.addAttribute("users", users);

        return "users";
    }
}

 */

