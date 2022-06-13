package com.telegrambot.botoldhouse.Service;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.telegrambot.botoldhouse.Parser.ParseToSeanse;
import com.telegrambot.botoldhouse.Repository.SeanseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private SeanseRepository seanseRepository;

    @Autowired
    private ParseToSeanse parseToSeanse;

    public Iterable<Seanse> getAll() throws IOException {  //потом убрать
        return seanseRepository.findAll();
    }




    public void updateDB() throws IOException {     // потом перенести в какой то другой класс
        List<Seanse> seanses = parseToSeanse.getFullAfisha();
        seanseRepository.deleteAll();
        seanseRepository.saveAll(seanses);
    }
}
