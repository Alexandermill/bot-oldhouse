package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.Constants.CalbackDataEnum;
import com.telegrambot.botoldhouse.Service.CacheSeanse;
import com.telegrambot.botoldhouse.Service.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.ReplyKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.IOException;

@Component
public class CallbackQueryHandler {

    @Autowired
    SeanseService seanseService;
    @Autowired
    ReplyKeyboardMaker replyKeyboardMaker;
    @Autowired
    CacheSeanse cacheSeanse;

    public BotApiMethod<?> CallbackQueryAnswer (CallbackQuery callbackQuery) throws IOException {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String[] arrData = callbackQuery.getData().split("#>");
        if (arrData[0].equals(CalbackDataEnum.ALL_SEANSES.name())) {
            int page = Integer.parseInt(arrData[1]);
            int month = Integer.parseInt(arrData[2]);

            return (seanseService.getEditMessage(month, page, chatId, messageId));

        } else if (arrData[0].equals(CalbackDataEnum.GET_MSG.name())) {
            int page = Integer.parseInt(arrData[1]);
            int month = Integer.parseInt(arrData[2]);
            String name = arrData[3];
            SendMessage sendMessage = seanseService.getMsgByMontAndNamePageble(month, chatId, name, page);
            return sendMessage;

        } else if (arrData[0].equals(CalbackDataEnum.ONE_SEANSE.name())) {
            int page = Integer.parseInt(arrData[1]);
            int month = Integer.parseInt(arrData[2]);
            String name = arrData[3];
            return (seanseService.getEditMessage(month, page, chatId, name, messageId));

        } else if (callbackQuery.getData().equals(CalbackDataEnum.BY_NAME.name())){
            SendMessage sendMessage = new SendMessage(chatId, "Выберете месяц чтобы посмотреть афишу");
            sendMessage.setReplyMarkup(replyKeyboardMaker.getSeanseByNameKeybord());
            return sendMessage;
        } else if (callbackQuery.getData().equals(CalbackDataEnum.LIST_ALL.name())){
            SendMessage sendMessage = new SendMessage(chatId, "Выберете месяц чтобы посмотреть афишу");
            sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
            return sendMessage;
        }

            return null;
        }



}