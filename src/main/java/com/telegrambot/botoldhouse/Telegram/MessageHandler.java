package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.Service.AdminService;
import com.telegrambot.botoldhouse.Service.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.ReplyKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

import static com.telegrambot.botoldhouse.Telegram.OldHouseBot.userLogger;

@Component
public class MessageHandler {

    @Autowired
    private ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    private SeanseService seanseService;

    @Autowired
    private AdminService adminService;

    private LocalDate ld = LocalDate.now();
    private String[] months = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd ММММ yyyy", new Locale("ru"));
    private int numCurrentMonth = ld.getMonth().getValue();
    private String currentMonth = months[ld.getMonth().getValue()];
    private String nextMonth = months[ld.plusMonths(1).getMonth().getValue()];
    private String nextMonth2 = months[ld.plusMonths(2).getMonth().getValue()];
    private String nextMonth3 = months[ld.plusMonths(3).getMonth().getValue()];
    private String nextMonth4 = months[ld.plusMonths(4).getMonth().getValue()];
    private String nextMonth5 = months[ld.plusMonths(5).getMonth().getValue()];


    public SendMessage answerMessage(Message message) throws IOException {

        String chatId = message.getChatId().toString();
        String inputText = message.getText();

        userLogger.debug("User {}, {}, {} ввел {}",
                message.getChatId(),
                message.getChat().getFirstName(),
                message.getChat().getLastName(),
                message.getText());

        adminService.saveUser(String.valueOf(message.getChatId()), message.getChat().getFirstName(), message.getChat().getLastName());

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {

            SendMessage sendMessage = new SendMessage();
            sendMessage = getStartMessage(chatId);
            return sendMessage;

        } else if (inputText.equals(currentMonth) | inputText.equals(nextMonth) | inputText.equals(nextMonth2)
                    | inputText.equals(nextMonth3) | inputText.equals(nextMonth4) | inputText.equals(nextMonth5)) {

            SendMessage sendMessage = seanseService.getByMontPageble(monthTonum(inputText), chatId, 0);
            return sendMessage;

        } else {
            SendMessage messageList = new SendMessage(chatId, "Используйте клавиатуру");
            return messageList;
        }

    }


    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Воспользуйтесь клавиатурой чтобы получить Афишу на месяц!");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private int monthTonum(String monthText){
        return Arrays.asList(months).indexOf(monthText);
    }



}
