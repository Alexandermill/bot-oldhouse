package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.Constants.MSG_Enum;
import com.telegrambot.botoldhouse.Service.AdminService;
import com.telegrambot.botoldhouse.Service.CacheSeanse;
import com.telegrambot.botoldhouse.Service.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.InlineKeybords;
import com.telegrambot.botoldhouse.Telegram.Keybords.ReplyKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static com.telegrambot.botoldhouse.Telegram.OldHouseBot.userLogger;

@Component
public class MessageHandler {

//    @Autowired
//    private ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    private InlineKeybords inlineKeybords;

    @Autowired
    private SeanseService seanseService;

    @Autowired
    private CacheSeanse cacheSeanse;

    @Autowired
    private AdminService adminService;

    private LocalDate ld = LocalDate.now();
    private String[] months = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};

    public SendMessage answerMessage(Message message) throws IOException {

        String chatId = message.getChatId().toString();
        String inputText = message.getText();

        userLogger.debug("User {}, {}, {} ввел {}",
                message.getChatId(),
                message.getChat().getFirstName(),
                message.getChat().getLastName(),
                message.getText());

        adminService.saveUser(String.valueOf(message.getChatId()), message.getChat().getFirstName(),
                                message.getChat().getLastName());

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {

            SendMessage sendMessage = getStartMessage(chatId);
            return sendMessage;

        } else if (cacheSeanse.ifExistByMonth(inputText)) {

            SendMessage sendMessage = seanseService.getByMontPageble(monthTonum(inputText), chatId, 0);
            return sendMessage;

        } else  if (cacheSeanse.ifExistByMonth(inputText.substring(0, inputText.length()-1)) & inputText.contains("_")) {

            SendMessage sendMessage = seanseService.getNameSensesInMonth(monthTonum(inputText.substring(0, inputText.length()-1)), chatId);
            return sendMessage;

        } else {
            SendMessage messageList = new SendMessage(chatId, "Используйте клавиатуру");
            return messageList;
        }

    }


    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, MSG_Enum.START_MSG.getMsg());
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(inlineKeybords.getStartInlineButtons());
        return sendMessage;
    }

    private int monthTonum(String monthText){
        return Arrays.asList(months).indexOf(monthText);
    }



}
