package com.telegrambot.botoldhouse.Service;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.telegrambot.botoldhouse.Entity.UserBot;
import com.telegrambot.botoldhouse.Parser.ParseToSeanse;
import com.telegrambot.botoldhouse.Repository.SeanseRepository;
import com.telegrambot.botoldhouse.Repository.UserBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private SeanseRepository seanseRepository;

    @Autowired
    private UserBotRepository userBotRepository;

    @Autowired
    private ParseToSeanse parseToSeanse;

    @Autowired
    private ExistSeanse existSeanse;

    public Iterable<Seanse> getAll() throws IOException {
        return seanseRepository.findAll();
    }

    public Iterable<UserBot> getUsers() throws IOException {
        return userBotRepository.findAll();
    }

    public void saveUser (String chatId, String firstName, String lastName){
        if (userBotRepository.findByChatId(chatId) == null){
            UserBot userBot = new UserBot(chatId, firstName, lastName);
            userBotRepository.save(userBot);
        }
    }


    @Scheduled(cron = "${interval-in-cron}")
    public void updateDB() throws IOException {
        List<Seanse> seanses = parseToSeanse.getFullAfisha();
        seanseRepository.deleteAll();
        seanseRepository.saveAll(seanses);
        existSeanse.getExistSeansesFromDB();
    }
}
