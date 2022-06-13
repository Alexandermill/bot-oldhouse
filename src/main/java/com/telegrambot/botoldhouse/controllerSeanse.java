package com.telegrambot.botoldhouse;

import com.telegrambot.botoldhouse.Entity.Seanse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/db")
public class controllerSeanse {

    @Autowired
    SeanseService seanseService;

    @Autowired
    SeanseRepository seanseRepository;

//    @GetMapping
//    public String allDB() throws IOException {
//        seanseService.addSeansesDB();
//        return "saved";
//    }

    @GetMapping ("/seanses")
    public Iterable<Seanse> getall() throws IOException {
        return seanseService.getAll();
    }

    @GetMapping ("/seanse")
    public Iterable<Seanse> getall(@RequestParam int mont) throws IOException {
        return seanseRepository.findSeanseByMontPageble(mont, PageRequest.of(0, 4));
    }

    @GetMapping ("/db")
    public String updateDB() throws IOException {
        seanseService.updateDB();
        return "updated";
    }


}
