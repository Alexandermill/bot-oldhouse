package com.telegrambot.botoldhouse.Controllers;

import com.telegrambot.botoldhouse.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class WebController {

    @Autowired
    AdminService adminService;

    @GetMapping(value = "/web/seanses")
    public String allSeanses(Model model) throws IOException {
        model.addAttribute("seanses", adminService.getAll());
        return "seanses";
    }

    @GetMapping(value = "/web/users")
    public String allUsers(Model model) throws IOException {
        model.addAttribute("users", adminService.getUsers());
        return "users";
    }

}
