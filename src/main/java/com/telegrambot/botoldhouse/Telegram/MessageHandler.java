package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.Constants.MSG_Enum;
import com.telegrambot.botoldhouse.Service.AdminService;
import com.telegrambot.botoldhouse.Service.CacheSeanse;
import com.telegrambot.botoldhouse.Service.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.InlineKeybords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    @Value("${telegram.admin-chat-id}")
    private String adminChatId;

    @Value("${telegram.support-chat-id}")
    private String supportChatId;

    @Value("${telegram.bot-name}")
    private String botName;

    private LocalDate ld = LocalDate.now();
    private String[] months = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};


    public SendMessage answerMessage(Message message) throws IOException {

        String chatId = message.getChatId().toString();
        String inputText = message.getText();
        if(chatId.substring(0, 1).equals("-")){
            inputText = message.getText().replaceAll(botName, "");
        }
        Map<String, String> roles = new HashMap<>();
        roles.put(adminChatId, "admin");
        
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

        } else  if (cacheSeanse.ifExistByMonth(inputText.substring(0, inputText.length()-1)) && inputText.contains("_")) {

            SendMessage sendMessage = seanseService.getNameSensesInMonth(monthTonum(inputText.substring(0, inputText.length()-1)), chatId);
            
            return sendMessage;

        } else if (inputText.equals("/support")) {

            SendMessage sendMessage = getSupportMessage(chatId);
            return sendMessage;

        } else if (inputText.length() > 8 && inputText.substring(0, 8).equals("/support")) {

            SendMessage sendMessage = new SendMessage(supportChatId, inputText);

            return sendMessage;

        } else if (roles.get(chatId) != null && roles.get(chatId).equals("admin") && inputText.substring(0, 5).equals("/mess")) {
            String[] messageArray = inputText.substring(6).split(";");
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(messageArray[0]);
            sendMessage.setText(messageArray[1]);
            return sendMessage;

        } else {
            SendMessage messageList = new SendMessage(chatId, "Используйте клавиатуру");
            return messageList;
        }

    }


    private SendMessage getSupportMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, MSG_Enum.SUPPORT_MSG.getMsg());
        sendMessage.enableHtml(true);
        return sendMessage;
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
