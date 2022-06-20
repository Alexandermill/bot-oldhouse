package com.telegrambot.botoldhouse;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.telegrambot.botoldhouse.Entity.UserBot;
import com.telegrambot.botoldhouse.Repository.SeanseRepository;
import com.telegrambot.botoldhouse.Service.AdminService;
import com.telegrambot.botoldhouse.Service.SeanseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/db")
public class controllerSeanse {

    @Autowired
    SeanseService seanseService;

    @Autowired
    SeanseRepository seanseRepository;

    @Autowired
    AdminService adminService;

//    @GetMapping
//    public String allDB() throws IOException {
//        seanseService.addSeansesDB();
//        return "saved";
//    }

    @GetMapping ("/seanses")
    public Iterable<Seanse> getall() throws IOException {
        return adminService.getAll();
    }

    @GetMapping ("/users")
    public Iterable<UserBot> getUsers() throws IOException {
        return adminService.getUsers();
    }

    @GetMapping ("/delete")
    public String deleteall() throws IOException {
        adminService.deleteAllSeanses();
        if (adminService.getAll() == null) {
            return "deleted";
        } else return "not deleted";
    }

    @GetMapping ("/seanse")
    public Iterable<Seanse> getall(@RequestParam int mont, @RequestParam int page) throws IOException {
        return seanseRepository.findSeansesByMontPageble(mont, PageRequest.of(page, 4));
    }

    @GetMapping ("/db")
    public String updateDB() throws IOException {
        adminService.updateDB();
        return "updated";
    }


}
