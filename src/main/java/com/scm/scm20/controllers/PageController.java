package com.scm.scm20.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;

import com.scm.scm20.entities.User;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page requested");
        model.addAttribute("name", "Welcome to the Home Page!");
        model.addAttribute("YouTubeChannel", "Home");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        System.out.println("About page requested");
        model.addAttribute("isLogin", true);
        model.addAttribute("YouTubeChannel", "About");
        return "about";
    }

    @RequestMapping("/services")
    public String services(Model model) {
        System.out.println("Services page requested");
        model.addAttribute("name", "Welcome to the Services Page!");
        model.addAttribute("YouTubeChannel", "Services");
        return "services";
    }

    @RequestMapping("/contact")
    public String contact(Model model) {
        System.out.println("Contact page requested");
        model.addAttribute("name", "Welcome to the Contact Page!");
        model.addAttribute("YouTubeChannel", "Contact");
        return "contact";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        System.out.println("Login page requested");
        model.addAttribute("name", "Welcome to the Login Page!");
        model.addAttribute("YouTubeChannel", "Login");
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        System.out.println("Register page requested");

        UserForm userForm = new UserForm();
        // userForm.setName("Raj");
        // userForm.setEmail("raj@example.com");
        // userForm.setPassword("password123");
        // userForm.setPhoneNumber("1234567890");
        // userForm.setAbout("I am Raj, a software developer.");
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String doRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {

        if (rBindingResult.hasErrors()) {
            return "register";
        }
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://avatar.iran.liara.run/public/4")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://avatar.iran.liara.run/public/4");

        userService.saveUser(user);

        Message message = Message.builder().content("Registeration successful").type(MessageType.green).build();

        System.out.println(message.getType() + " set in session");

        session.setAttribute("message", message);
        return "redirect:/register";
    }
}
