package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.ReplyKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class MessageHandler {

    @Autowired
    private ReplyKeyboardMaker replyKeyboardMaker;

    OldHouseBot oldHouseBot;

    @Autowired
    private SeanseService seanseService;

    private LocalDate ld = LocalDate.now();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM", new Locale("ru"));
    String montPlus1 = ld.plusMonths(1).format(dtf);


    public BotApiMethod<?> answerMessage(Message message) throws IOException {
        String chatId = message.getChatId().toString();

        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            SendMessage sendMessage = new SendMessage(chatId, "Афиша на июнь");
            sendMessage.enableMarkdown(true);
            KeyboardRow row1 = new KeyboardRow();
            row1.add(new KeyboardButton(ld.format(dtf)));
            row1.add(new KeyboardButton(montPlus1));


            List<KeyboardRow> keyboard = new ArrayList<>();
            keyboard.add(row1);

            final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setKeyboard(keyboard);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            return sendMessage;
        } else if (inputText.equals("июня")) {

            List<SendMessage> messageList = seanseService.getByMont(6, chatId);


            return messageList.get(1);


        } else {
            return new SendMessage(chatId, "NON_COMMAND_MESSAGE");
        }
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "HELP_MESSAGE");
        sendMessage.enableMarkdown(true);
//        KeyboardRow row1 = new KeyboardRow();
//        row1.add(new KeyboardButton("Июня"));
//        row1.add(new KeyboardButton("Июль"));
//
//
//        List<KeyboardRow> keyboard = new ArrayList<>();
//        keyboard.add(row1);
//
//        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        replyKeyboardMarkup.setKeyboard(keyboard);
//        replyKeyboardMarkup.setSelective(true);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(false);
//        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

//    private SendMessage getTasksMessage(String chatId) {
//        SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.CHOOSE_DICTIONARY_MESSAGE.getMessage());
//        sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons(
//                CallbackDataPartsEnum.TASK_.name(),
//                dictionaryExcelService.isUserDictionaryExist(chatId)
//        ));
//        return sendMessage;
//    }


}
