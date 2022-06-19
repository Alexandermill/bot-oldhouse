package com.telegrambot.botoldhouse.Repository;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.telegrambot.botoldhouse.Entity.UserBot;
import org.springframework.data.repository.CrudRepository;

public interface UserBotRepository extends CrudRepository<UserBot, Long> {

    public UserBot findByChatId(String chatId);

}
