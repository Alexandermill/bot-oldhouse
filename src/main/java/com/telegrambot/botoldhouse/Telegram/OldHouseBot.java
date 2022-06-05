package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.ReplyKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class OldHouseBot extends SpringWebhookBot {
    private String botPath;
    private String botUsername;
    private String botToken;
    private LocalDate ld = LocalDate.now();
    private String[] monts = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd ММММ yyyy", new Locale("ru"));

    @Autowired
    SeanseService seanseService;

    @Autowired
    ReplyKeyboardMaker replyKeyboardMaker;

    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public OldHouseBot(SetWebhook setWebhook, MessageHandler messageHandler,CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public CallbackQueryHandler getCallbackQueryHandler() {
        return callbackQueryHandler;
    }

    public void setCallbackQueryHandler(CallbackQueryHandler callbackQueryHandler) {
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
//            return handleUpdate(update);
//            if (update.hasCallbackQuery()) {
//
//            }

            String chatId = update.getMessage().getChatId().toString();
            String inputText = update.getMessage().getText();

            if (inputText == null) {
                throw new IllegalArgumentException();
            } else if (inputText.equals("/start")) {
                SendMessage sendMessage = new SendMessage(chatId, "Афиша на месяц");
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
                return sendMessage;

            } else if (inputText.equals(monts[ld.getMonth().getValue()])) {

                List<SendMessage> messageList = seanseService.getByMont(ld.getMonth().getValue(), chatId);

                    for (SendMessage s: messageList){
                        System.out.println(s);
                        execute(s);
                    }
            } else if (inputText.equals(monts[ld.plusMonths(1).getMonth().getValue()])) {

                List<SendMessage> messageList = seanseService.getByMont(ld.plusMonths(1).getMonth().getValue(), chatId);

                for (SendMessage s: messageList){
                    System.out.println(s);
                    execute(s);
                }




            } else {
                return new SendMessage(chatId, "NON_COMMAND_MESSAGE");
            }
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "EXCEPTION_ILLEGAL_MESSAGE");
        } catch (Exception e) {
            System.out.println(e);
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "EXCEPTION_WHAT_THE_FUCK");
        }
        return null;
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException {

            Message message = update.getMessage();
            if (message != null)
            return messageHandler.answerMessage(update.getMessage());

        return null;
    }



}
