package com.mongotest.MonGo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongotest.MonGo.model.Photo;
import com.mongotest.MonGo.model.User;
import com.mongotest.MonGo.service.PhotoService;
import com.mongotest.MonGo.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PhotoService photoService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String Login() {
        log.info("Login screen access!");
        return "login/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(User user, HttpSession session, Model model, Photo photo) {
        Optional<User> result = userService.searchUser(user.getEmail());
        String email = result.map(User::getEmail).orElse("");
        if (email != "") {
            String password = result.map(User::getPassword).orElse("");
            if (user.getPassword().equals(password)) {
                log.info("Login Successfully!");
                session.setAttribute("loginInfo", email);
                return "redirect:/home";
            } else {
                log.info("User Password is worng!");
                return "redirect:/";
            }
        } else {
            log.info("This is not user!");
            return "redirect:/";
        }

    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String Create() {
        log.info("Register Screen access!");
        return "login/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String create(User user) {

        Optional<User> result = userService.searchUser(user.getEmail());
        String email = result.map(User::getEmail).orElse("");
        if (email == "") {
            userService.createUser(user);
            log.info("New User ok! " + user);
            return "redirect:/";
        } else {
            log.info(email + " already Exit!");
            return "redirect:/register";
        }
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loginInfo");
        if (email == null) {
            log.info("Login session is Null");
            return "redirect:/error";
        } else {
            List<Photo> photos = photoService.getAllPhotos();
            model.addAttribute("photos", photos);
            return "login/home";
        }
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "error";
    }

}
