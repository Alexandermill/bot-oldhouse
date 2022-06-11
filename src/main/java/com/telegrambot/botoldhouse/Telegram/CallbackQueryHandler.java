package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.SeanseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CallbackQueryHandler {

    @Autowired
    SeanseService seanseService;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws IOException {



            return null;
        }


}