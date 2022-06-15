package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.Service.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.ReplyKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.telegrambot.botoldhouse.Telegram.OldHouseBot.userLogger;

@Component
public class MessageHandler {

    @Autowired
    private ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    private SeanseService seanseService;

    private LocalDate ld = LocalDate.now();
    private String[] monts = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd ММММ yyyy", new Locale("ru"));
    private int numCurrentMonth = ld.getMonth().getValue();
    private String currentMonth = monts[ld.getMonth().getValue()];
    private String nextMonth = monts[ld.plusMonths(1).getMonth().getValue()];
    private String nextMonth2 = monts[ld.plusMonths(2).getMonth().getValue()];
    private String nextMonth3 = monts[ld.plusMonths(3).getMonth().getValue()];
    private String nextMonth4 = monts[ld.plusMonths(4).getMonth().getValue()];
    private String nextMonth5 = monts[ld.plusMonths(5).getMonth().getValue()];


    public List<SendMessage> answerMessage(Message message) throws IOException {

        String chatId = message.getChatId().toString();
        String inputText = message.getText();

        userLogger.debug("User {}, {}, {} ввел {}",
                message.getChatId(),
                message.getChat().getFirstName(),
                message.getChat().getLastName(),
                message.getText());

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {

            List<SendMessage> messageList = new ArrayList<>();
            messageList.add(getStartMessage(chatId));
            return messageList;

        } else if (inputText.equals(currentMonth)) {

            List<SendMessage> messageList = seanseService.getByMontPageble(numCurrentMonth, chatId, 1);
            return messageList;

        } else if (inputText.equals(nextMonth)) {

            List<SendMessage> messageList = seanseService.getByMontPageble(numCurrentMonth+1, chatId, 1);
            return messageList;

        } else if (inputText.equals(nextMonth2)) {

            List<SendMessage> messageList = seanseService.getByMontPageble(numCurrentMonth+2, chatId, 1);
            return messageList;

        } else if (inputText.equals(nextMonth3)) {

            List<SendMessage> messageList = seanseService.getByMontPageble(9, chatId, 1);
            return messageList;

        } else if (inputText.equals(nextMonth4)) {

            List<SendMessage> messageList = seanseService.getByMontPageble(numCurrentMonth+4, chatId, 1);
            return messageList;

        } else if (inputText.equals(nextMonth5)) {

            List<SendMessage> messageList = seanseService.getByMontPageble(numCurrentMonth+5, chatId, 1);
            return messageList;

        } else {
            List<SendMessage> messageList = new ArrayList<>();
            messageList.add(new SendMessage(chatId, "Используйте клавиатуру"));
            return messageList;
        }

    }




    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Воспользуйтесь клавиатурой чтобы получить Афишу на месц!");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }



}
