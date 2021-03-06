package org.example.controller;

import org.example.service.UserService;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    private  PasswordEncoder passwordEncoder;

    /*@Autowired
    public AdminController(@Lazy UserService userService, @Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }*/

    @GetMapping("/allUsers")//полный список
    public String list(Model model) {
        model.addAttribute("users", userService.usersList());
        return "list";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("/new") //форма для нового юзера
    public String newUser(Model model) {           //@ModelAttribute
        model.addAttribute("user", new User());

        return "new";
    }

    @PostMapping//перенаправление на страницу всех юзеров
    public String create(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/admin/allUsers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/allUsers";
    }

}
